package com.ytrain.wxns.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.ssy.utils.Constants;
import com.ssy.utils.ExitApp;
import com.ytrain.wxns.R;
import com.ytrain.wxns.adapter.PartPopAdapter;
import com.ytrain.wxns.asynloading.AsynDownloadTask;
import com.ytrain.wxns.asynloading.AsynDownloadTask.MyCallback;
import com.ytrain.wxns.asynloading.GlideRoundTransform;
import com.ytrain.wxns.database.InitDataSqlite;
import com.ytrain.wxns.entity.ArticleEntity;
import com.ytrain.wxns.entity.PartEntity;
import com.ytrain.wxns.utils.ConnectorService;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;

@SuppressLint("HandlerLeak")
public class PartList extends Activity {
	private ImageView tvPbi;
	private TextView tvTitle, tvBack, ivList;
	private PopupWindow window;
	private int partId = 0;
	private Bundle bundle;
	private PartPopAdapter adapterPop;
	private int parentId = 0;
	private String name = "智慧南山";
	private int position = 0, sposition = 0;

	private ListView lvResult;
	private GridView lstPop;
	private List<PartEntity> lstPart = null;
	private MyAdapter adapter;
	private ArrayList<ArticleEntity> arts;
	private AsynDownloadTask<ArticleEntity> task;
	View view = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.part_list);
		ExitApp.getInstance().addActivity(this);
		Constants.SetTitle(this);
		/* 获取传过来的参数 */
		bundle = this.getIntent().getExtras();
		parentId = bundle.getInt("id", 0);
		name = bundle.getString("name");
		Constants.SetTitle(this);

		initView();
		initData();
		initListener();
	}

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(this);
		view = inflater.inflate(R.layout.partlist_pop, null);

		lstPop = (GridView) view.findViewById(R.id.gv_pop);
		adapterPop = new PartPopAdapter(this, getNavData());
		lstPop.setAdapter(adapterPop);
		lstPop.setFocusableInTouchMode(true);
		lstPop.setFocusable(true);
		initPopMenu();
		// 返回按钮
		tvBack = (TextView) findViewById(R.id.tvBack);
		// 设置顶部的标题
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText(name);
		// 更多菜单按钮
		ivList = (TextView) findViewById(R.id.ivList);
		// 数据ListView
		lvResult = (ListView) this.findViewById(R.id.listView1);
		if (null != lstPart && lstPart.size() > 0) {
			PartEntity pe = lstPart.get(0);
			partId = bundle.getInt("partId", pe.getId());
			String partImgUrl = bundle.getString("partImgUrl");
			partImgUrl = partImgUrl == null ? pe.getImageUrl() : partImgUrl;
			tvPbi = (ImageView) findViewById(R.id.partImg);
			Glide.with(this).load(Constants.dataUrl + "/" + partImgUrl).centerCrop().into(tvPbi);
		}
	}

	private void initData() {
		arts = new ArrayList<ArticleEntity>();
		adapter = new MyAdapter(this, arts);
		task = new AsynDownloadTask<ArticleEntity>(this, lvResult, adapter, arts);
		task.setCallback(new MyCallback<ArticleEntity>() {
			@Override
			public List<ArticleEntity> getData() {
				List<ArticleEntity> list = null;
				try {
					InitDataSqlite idsqlite = new InitDataSqlite(PartList.this);
					// 获取7天内的缓存
					list = idsqlite.findCacheByPartId(partId);
					// 如果有网络的情况的下
					if (Constants.isExistNetwork(PartList.this)) {
						Integer[] ids = null;// 存放7天内的数据唯一标识
						if (list != null && list.size() > 0) {
							ids = new Integer[list.size()];
							int i = 0;
							for (ArticleEntity ae : list) {
								ids[i] = ae.getId();
								i++;
							}
							if (ids != null && ids.length > 0) {
								Arrays.sort(ids);
							}
						}
						// 从服务器抓取数据
						String text = ConnectorService.getInstance().findArticlePageByPartId(partId, task.currentPage);
						
						JSONArray array = JSON.parseArray(text);
						int size = array.size();
						list = new ArrayList<ArticleEntity>();
						ArticleEntity art = null;
						int location = 0;
						List<ArticleEntity> cacheList = new ArrayList<ArticleEntity>();
						for (int i = 0; i < size; i++) {
							art = array.getObject(i, ArticleEntity.class);
							list.add(art);
							// 判断是否需要把没有缓存的数据加入缓存
							if (ids != null && ids.length > 0) {
								location = Arrays.binarySearch(ids, art.getId());
								if (location < 0) {
									cacheList.add(art);
								} else {
									cacheList.add(art);
								}
							} else {
								cacheList.add(art);
							}
						}
						// 添加缓存
						if (cacheList.size() > 0) {
							idsqlite.saveArticle(cacheList);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}

			// 查找数据
			@Override
			public List<ArticleEntity> searchData() {
				return null;
			}

		});
		task.download();
	}

	private void initListener() {
		// 返回按钮监听器
		tvBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PartList.this.finish();
			}
		});
		// 数据列表监听器
		lvResult.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				position = arg2;
				showActivityArticle();
			}
		});
		// 导航栏按钮监听器
		ivList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!window.isShowing() && window != null) {
					window.showAsDropDown(ivList, 0, 0);
				}
			}
		});
	}

	private void initPopMenu() {
		window = new PopupWindow(view, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setOutsideTouchable(true);
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setAnimationStyle(android.R.style.Animation_Dialog);
		window.setTouchable(true);
		window.setFocusable(true);
		window.update();

		lstPop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				sposition = arg2;
				showActivityPart();
				window.dismiss();
			}
		});
		/**
		 * 1.解决再次点击MENU键无反应问题 2.view是PopupWindow的子View
		 */
		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (window.isShowing())) {
					window.dismiss();// 这里写明模拟menu的PopupWindow退出就行
					return true;
				}
				return false;
			}
		});

		if (window.isShowing()) {
			window.dismiss();
		}

	}

	/**
	 * 根据上级栏目编号获取栏目信息
	 * 
	 * @return
	 */
	private List<PartEntity> getNavData() {
		try {
			InitDataSqlite isqlite = new InitDataSqlite(this);
			lstPart = isqlite.findCacheByParentId(parentId);
			if (null == lstPart || lstPart.size() < 1) {// 如果有网络的情况的下
				if (Constants.isExistNetwork(PartList.this)) {
					String data = ConnectorService.getInstance().findPartByParentId(parentId);
					JSONArray array = JSON.parseArray(data);
					int size = array.size();

					PartEntity part = null;
					lstPart = new ArrayList<PartEntity>();
					for (int i = 0; i < size; i++) {
						part = array.getObject(i, PartEntity.class);
						part.setSsid("");
						lstPart.add(part);
					}
					if (lstPart != null && lstPart.size() > 0) {
						// 保存到本地
						isqlite.savePart(lstPart);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lstPart;
	}

	/**
	 * 刷新本页面的数据
	 */
	private void showActivityPart() {
		new Thread() {
			@Override
			public void run() {
				PartList.this.finish();
				PartEntity pe = lstPart.get(sposition);
				Intent intent = new Intent(PartList.this, PartList.class);
				intent.putExtra("partId", pe.getId());
				intent.putExtra("id", parentId);
				intent.putExtra("name", name);
				intent.putExtra("partImgUrl", pe.getImageUrl());
				startActivity(intent);
			}
		}.start();
	}

	/**
	 * 进入内容页面
	 */
	private void showActivityArticle() {
		new Thread() {
			@Override
			public void run() {
				ArticleEntity art = arts.get(position);
				Intent intent = null;
				if (parentId == 7) {
					String url = art.getSubtitle();
					intent = new Intent(Intent.ACTION_VIEW);
					intent.setClass(PartList.this, SiteNav.class);
					intent.setData(Uri.parse(url));
					intent.putExtra("url", url);
					intent.putExtra("title", art.getTitle());
				} else {
					intent = new Intent(PartList.this, Article.class);
					intent.putExtra("id", art.getId());
					String createTime = art.getCreateTime();
					if (createTime != null && createTime.length() > 11) {
						createTime = createTime.substring(0, 11);
					} else {
						createTime = "未知";
					}
					String source = art.getSource();
					source = source == null ? "未知" : source;
					String cs = "<h5>发表于:" + createTime + " 来源:" + source + "</h5>";
					String content = "<h3>" + art.getTitle() + "</h3>" + cs + art.getContent();
					intent.putExtra("content", content);
					intent.putExtra("name", name);
				}
				startActivity(intent);
			}
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PartList.this.finish();
	}

	class MyAdapter extends BaseAdapter {
		Activity activity;
		List<ArticleEntity> arts;

		public MyAdapter(Activity activity, List<ArticleEntity> arts) {
			this.activity = activity;
			this.arts = arts;
		}

		public void setQuotes(ArrayList<ArticleEntity> arts) {
			if (this.arts != null) {
				this.arts = arts;
			} else {
				this.arts = new ArrayList<ArticleEntity>();
			}

		}

		@Override
		public int getCount() {
			return arts.size();
		}

		@Override
		public ArticleEntity getItem(int position) {
			return arts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(activity);
				convertView = inflater.inflate(R.layout.items, null);
				holder = new Holder();
				holder.tvInfo = (TextView) convertView.findViewById(R.id.info);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
				holder.ivImg = (ImageView) convertView.findViewById(R.id.img);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();

			}
			// 设置item的背景色
//			if (position % 2 == 0) {
//				convertView.setBackgroundResource(R.color.list_item_1);
//			} else {
//				convertView.setBackgroundResource(R.color.list_item_2);
//			}
			ArticleEntity art = getItem(position);

			holder.tvTitle.setText(art.getTitle());
			holder.tvInfo.setText(art.getSubtitle());
			Glide.with(getApplicationContext()).load(Constants.dataUrl + "/" + art.getImageUrl())
					.transform(new GlideRoundTransform(activity)).into(holder.ivImg);
			return convertView;
		}

		class Holder {
			private TextView tvTitle, tvInfo;
			private ImageView ivImg;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		return false;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			PartList.this.finish();
		}
		return super.dispatchKeyEvent(event);
	}

	/** 创建MENU */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// 必须创建一项
		return super.onCreateOptionsMenu(menu);
	}

	/** 拦截MENU */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (!window.isShowing() && window != null) {
			window.showAsDropDown(ivList, 0, 0);
		}
		return false;// 返回为true 则显示系统menu
	}

}
