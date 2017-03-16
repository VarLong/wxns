package com.ytrain.wxns.asynloading;

import java.util.ArrayList;
import java.util.List;
import com.ssy.utils.Constants;
import com.ytrain.wxns.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 分页加载类，可通用
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "ShowToast", "HandlerLeak" })
public class AsynDownloadTask<E> {
	private final static int MSG_ADD = 1;
	private final static int TYPE_SEARCH = 3;
	private final static int TYPE_DOWNLOAD = 2;
	public int type;// 当前的下载类型，搜索或下载
	Activity activity;
	int maxCount;// 总数
	List<E> mList;
	public int currentPage;
	Thread workThread;
	AbsListView listview;
	BaseAdapter adapter;
	boolean isLoop;// 没有加载完所有的数据之前为true,加载完成后为false
	boolean isDownloading;// 是否正在下载,true表示正在下载
	boolean isOverDownload;
	View loadmore;
	Button btnGetMore;
	LinearLayout llLoading;
	int count = 0;
	@SuppressWarnings("rawtypes")
	MyCallback callback = null;

	Handler mHandler = new Handler() {
		@Override
		@SuppressWarnings({ "unchecked" })
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_ADD:
				List<E> list = (ArrayList<E>) msg.obj;
				// 没有数据，直接退出
				if ((list == null || list.isEmpty()) && currentPage == 1) {

					if (count == 1) {
						download();
					} else {
						llLoading.setVisibility(View.GONE);
						btnGetMore.setVisibility(View.VISIBLE);
						btnGetMore.setText("当前没有任何数据");
						btnGetMore.setClickable(false);
						quit();
					}
					return;
				}
				// 最后一页，且最后一页刚好是20条
				if (list == null && currentPage > 1) {// 最后一页刚好是20的情况
					overDownload();
					return;
				}
				// 将数据放入到list中;
				mList.addAll(list);
				// 最后一页少于20条
				int size = mList.size();
				// 最后一条少于20的情况
				if (!list.isEmpty() && size / Constants.PAGE_SIZE < currentPage) {
					isOverDownload = true;
					overDownload();
				}
				if (!list.isEmpty() && Constants.isExistNetwork() == false) {
					isOverDownload = true;
					overDownload();
				}
				currentPage++;
				// 通知adapter数据集发送变化
				adapter.notifyDataSetChanged();
				if (isOverDownload) {
					overDownload();
				}
				break;
			}
		}
	};

	@SuppressLint({ "HandlerLeak", "NewApi" })
	public AsynDownloadTask(final Activity activity, AbsListView listview, final BaseAdapter adapter, List<E> list) {
		// 加载视图
		LayoutInflater inflater = LayoutInflater.from(activity);
		loadmore = inflater.inflate(R.layout.load_more, null);
		btnGetMore = (Button) loadmore.findViewById(R.id.btn_load_more);
		llLoading = (LinearLayout) loadmore.findViewById(R.id.ll_load_more);
		this.listview = listview;
		if (listview instanceof ListView) {
			((ListView) listview).addFooterView(loadmore);
			((ListView) this.listview).setAdapter(adapter);
		} else if (listview instanceof GridView) {
			((GridView) this.listview).setAdapter(adapter);
		}
		this.type = TYPE_DOWNLOAD;
		this.currentPage = 1;
		this.isLoop = true;
		this.isDownloading = true;
		this.isOverDownload = false;
		this.activity = activity;
		this.adapter = adapter;
		this.mList = list;

		this.workThread = new Thread() {
			@Override
			public void run() {

				// 第一次
				// 先加载一些数据更新
				while (isLoop) {
					if (isDownloading) {
						try {
							List<E> list = null;
							if (type == TYPE_SEARCH) {
								list = searchData();
							} else if (type == TYPE_DOWNLOAD) {
								list = getData();
							}

							Message msg = Message.obtain();
							msg.what = MSG_ADD;
							msg.obj = list;
							mHandler.sendMessageDelayed(msg, 200);

							count++;

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							isDownloading = false;
						}
					}
					while (!isLoop) {
						break;
					}
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		this.workThread.start();
		initListener();

	}

	/**
	 * 全部加载完成以后
	 */
	protected void overDownload() {
		isLoop = false;
		isDownloading = false;
		btnGetMore.setVisibility(View.VISIBLE);
		llLoading.setVisibility(View.GONE);
		btnGetMore.setText("全部加载完成");
		btnGetMore.setClickable(false);
		quit();

	}

	/**
	 * 结束工作线程
	 */
	public void quit() {
		if (isLoop) {
			isLoop = false;
		}
		if (isDownloading) {
			isDownloading = false;
		}
		synchronized (workThread) {
			workThread.notify();
		}
	}

	/**
	 * 设置回调
	 * 
	 * @param callback
	 */
	@SuppressWarnings("rawtypes")
	public void setCallback(MyCallback callback) {
		this.callback = callback;
	}

	private void initListener() {

		this.btnGetMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnGetMore.setVisibility(View.GONE);
				llLoading.setVisibility(View.VISIBLE);
				llLoading.setClickable(false);
				llLoading.setFocusable(false);
				llLoading.setPressed(false);
				download();
			}
		});
		this.listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					if (isLoop == false) {
						return;
					}
					// 判断网络
					if (Constants.isExistNetwork(activity) == false) {
						return;
					}
					int type = Constants.getNetworkType(activity);
					if (type == ConnectivityManager.TYPE_WIFI) {// wifi网络
						btnGetMore.setVisibility(View.GONE);
						llLoading.setVisibility(View.VISIBLE);
						llLoading.setClickable(false);
						llLoading.setFocusable(false);
						llLoading.setPressed(false);
						download();

					} else {
						btnGetMore.setVisibility(View.VISIBLE);// 其他网络
						llLoading.setVisibility(View.GONE);
						llLoading.setClickable(false);
						llLoading.setFocusable(false);
						llLoading.setPressed(false);
						download();
					}

				}
			}
		});

	}

	/**
	 * 开始下载，会唤醒
	 */
	public void download() {
		this.type = TYPE_DOWNLOAD;
		isDownloading = true;
		if (currentPage == 1) {
			btnGetMore.setVisibility(View.GONE);
			llLoading.setVisibility(View.VISIBLE);
			llLoading.setClickable(false);
			llLoading.setFocusable(false);
			llLoading.setPressed(false);
		}
		synchronized (workThread) {
			workThread.notify();

		}
	}

	/**
	 * 获取数据
	 * 
	 * @param keyword
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<E> getData() {
		List<E> list = null;
		try {
			list = callback.getData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 开始搜索
	 * 
	 * @return
	 */
	public void startsearch() {
		// 还原属性值
		this.type = TYPE_SEARCH;
		if (currentPage == 1) {
			btnGetMore.setVisibility(View.GONE);
			llLoading.setVisibility(View.VISIBLE);
			llLoading.setClickable(false);
			llLoading.setFocusable(false);
			llLoading.setPressed(false);
		}

		synchronized (workThread) {
			workThread.notify();
		}
	}

	/**
	 * 查找数据
	 * 
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	private List<E> searchData() {
		List<E> list = null;
		try {
			list = callback.searchData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 外部调用，在工作线程中执行
	 * 
	 * @author Administrator
	 * 
	 * @param <E>
	 */
	public interface MyCallback<E> {
		List<E> getData();// 用于分页加载
		List<E> searchData();// 用于搜索数据
	}

	
}