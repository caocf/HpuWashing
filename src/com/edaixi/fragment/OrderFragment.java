package com.edaixi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.edaixi.activity.LoginActivity;
import com.edaixi.activity.MainActivity;
import com.edaixi.activity.OrderDetialActivity;
import com.edaixi.activity.R;
import com.edaixi.adapter.OrderListAdapter;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.modle.OrderListItemBean;
import com.edaixi.util.Constants;
import com.edaixi.util.LogUtil;
import com.edaixi.util.OrderListAdapterEvent;
import com.edaixi.util.ParseOrderList;
import com.edaixi.util.SaveUtils;
import com.edaixi.view.SharePacketsDialog;
import com.edaixi.view.XListView;
import com.edaixi.view.XListView.IXListViewListener;
import com.tendcloud.appcpa.Order;
import com.tendcloud.appcpa.TalkingDataAppCpa;
import com.tendcloud.tenddata.TCAgent;
import de.greenrobot.event.EventBus;

public class OrderFragment extends BaseFragment implements IXListViewListener {
	public static final int TUREMESSAGESERVING = 1;
	public static final int ERRORMESSAGESERVING = 2;
	public static final int TUREMESSAGECOMPLETED = 3;
	public static final int ERRORMESSAGECOMPLETED = 4;
	public static final int TUREMESSAGEREMOVE = 5;
	public static final int ERRORMESSAGEREMOVE = 6;
	protected static final String TAG = "OrderFragment";
	protected static final String SERINGORDER = "SERINGORDER";
	protected static final String COMPLETEDORDER = "COMPLETEDORDER";
	private static final int TUREMESSAGESERVINGMORE = 7;
	private static final int ERRORMESSAGESERVINGMORE = 8;
	private static final int SUCESSMSGGMORE = 9;
	private static final int FAILMSGMORE = 10;
	private int pageTagServing = 1;
	private int pageTagCompleted = 1;
	private int perPage = 5;
	private com.edaixi.view.XListView order_xListView;
	private RadioButton serving_btn;
	private RadioButton completed_btn;
	private TextView order_login_tips;
	private TextView order_login_btn;
	private OrderListAdapter orderAdapterServing;
	private OrderListAdapter orderAdapterCompleted;
	private ImageView iv_no_order;
	private ImageView iv_loading;
	private ImageView iv_no_net_wififail;
	private Activity mActivity;
	private ParseOrderList parseOrderList;
	private ArrayList<OrderListItemBean> servingOrderList;
	private ArrayList<OrderListItemBean> completedOrderList;
	private HashMap<String, String> orderListParams = new HashMap<String, String>();
	private HashMap<String, String> sharearm = new HashMap<String, String>();
	public SaveUtils saveUtils;
	private boolean XListViewTag = true;;
	private boolean isShowSharedialog = false;
	private boolean isShowPage;
	private String share_coupon_total = null;
	private String share_order_url = null;
	private String share_title = null;
	private String share_content = null;
	private String share_image_url = null;
	private AppConfig appConfig;
	private Handler mHandler;
	@SuppressWarnings("unused")
	private AnimationDrawable animationDrawable;
	private static final int GETISORDERSHARESUCESS = 100;
	private static final int GETISORDERSHAREFAIL = -100;

	public OrderFragment(int titleId) {
	}

	public OrderFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appConfig = AppConfig.getInstance();
		parseOrderList = new ParseOrderList();
		servingOrderList = new ArrayList<OrderListItemBean>();
		completedOrderList = new ArrayList<OrderListItemBean>();
		EventBus.getDefault().register(this);
	}

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		private boolean isLoadMoreCom;
		private boolean isLoadMore;

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String servingResultSucess = (String) msg.obj;
				if (servingResultSucess != null) {
					servingOrderList = new ArrayList<OrderListItemBean>();
					servingOrderList = parseOrderList
							.parseOrderList(servingResultSucess.toString());
					if (servingOrderList != null && servingOrderList.size() > 0) {
						iv_no_order.setVisibility(View.GONE);
						iv_loading.setVisibility(View.GONE);
						iv_no_net_wififail.setVisibility(View.GONE);
						if (orderAdapterServing == null
								&& servingOrderList.size() > 0) {
							orderAdapterServing = new OrderListAdapter(
									servingOrderList, getActivity(),
									SERINGORDER);
							if (servingOrderList.size() > 4) {
								order_xListView.setPullLoadEnable(true);
								order_xListView.haveOrderShow();
							}
							order_xListView.setAdapter(orderAdapterServing);
							orderAdapterServing.notifyDataSetChanged();
							order_xListView
									.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> parent,
												View view, int position, long id) {
											TCAgent.onEvent(getActivity(),
													"衣物详情");
											Intent orderDetail = new Intent();
											orderDetail.setClass(mActivity,
													OrderDetialActivity.class);
											Bundle bundle = new Bundle();
											if (position > 0) {
												bundle.putSerializable(
														"OrderListItembean",
														servingOrderList
																.get(position - 1));
												orderDetail.putExtras(bundle);
												mActivity
														.startActivity(orderDetail);
											}
										}
									});
							if (AppConfig.getInstance().isNewOrder()) {
								Order order = Order
										.createOrder(
												servingOrderList.get(0)
														.getOrder_sn(),
												(int) Double
														.parseDouble(servingOrderList
																.get(0)
																.getOrder_price()),
												"CNY")
										.addItem(
												servingOrderList.get(0)
														.getCategory_id(),
												"eDaiXiOrder",
												(int) Double
														.parseDouble(servingOrderList
																.get(0)
																.getOrder_price()) * 100,
												1);
								TalkingDataAppCpa.onPlaceOrder(
										saveUtils.getStrSP(KeepingData.PHONE),
										order);
							}
							AppConfig.getInstance().setNewOrder(false);
						}
						if (isShowPage && (servingOrderList.size() > 0)
								&& isShowShareDialog()) {
							if ((share_coupon_total != null)
									&& (share_order_url != null)) {
								SharePacketsDialog showShare = new SharePacketsDialog(
										getActivity(),
										R.style.customdialog_style,
										R.layout.share_packets_dialog,
										share_coupon_total, share_order_url,
										share_title, share_content,
										share_image_url);
								showShare.show();
								isShowSharedialog = false;
							}
						}

					} else {
						order_login_btn.setVisibility(View.GONE);
						order_login_tips.setVisibility(View.GONE);
						iv_no_order.setVisibility(View.VISIBLE);
						iv_no_order.setImageResource(R.drawable.no_order);
						order_xListView.setVisibility(View.GONE);
					}
				}
				break;
			case 2:
				break;
			case 3:
				String completedResultScuess = (String) msg.obj;
				if (completedResultScuess != null) {
					completedOrderList = new ArrayList<OrderListItemBean>();
					completedOrderList = parseOrderList
							.parseOrderList(completedResultScuess.toString());
					if (completedOrderList != null
							&& completedOrderList.size() > 0) {
						iv_no_order.setVisibility(View.GONE);
						iv_loading.setVisibility(View.GONE);
						iv_no_net_wififail.setVisibility(View.GONE);
						order_xListView.setVisibility(View.VISIBLE);
						iv_no_order.setVisibility(View.GONE);
						if (orderAdapterCompleted == null) {
							orderAdapterCompleted = new OrderListAdapter(
									completedOrderList, getActivity(),
									COMPLETEDORDER);
							if (completedOrderList.size() > 4) {
								order_xListView.setPullLoadEnable(true);
								order_xListView.haveOrderShow();
							} else {

							}
							order_xListView.setAdapter(orderAdapterCompleted);
						}

						order_xListView
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> parent, View view,
											int position, long id) {
										Intent orderDetail = new Intent();
										orderDetail.setClass(mActivity,
												OrderDetialActivity.class);
										Bundle bundle = new Bundle();
										if (position > 0) {
											bundle.putSerializable(
													"OrderListItembean",
													completedOrderList
															.get(position - 1));
											orderDetail.putExtras(bundle);
											mActivity
													.startActivity(orderDetail);
										}
									}
								});
					} else {
						order_login_btn.setVisibility(View.GONE);
						order_login_tips.setVisibility(View.GONE);
						iv_no_order.setVisibility(View.VISIBLE);
						iv_no_order.setImageResource(R.drawable.no_order);
						order_xListView.setVisibility(View.GONE);
					}
				}
				break;
			case 4:
				break;
			case 5:
				String removeResultSucess = (String) msg.obj;
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					if (removeResultSucess != null
							&& jsonObject.getBoolean("ret")) {
						EventBus.getDefault().post(
								new OrderListAdapterEvent("ShanChuDingDan"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case 6:
				String removeResultFail = (String) msg.obj;
				if (removeResultFail != null) {
					((MainActivity) getActivity()).showdialog("删除订单失败，稍后重试...");
				}
				break;
			case 7:
				String loadMoreResultSucess = (String) msg.obj;
				if (loadMoreResultSucess != null) {
					ArrayList<OrderListItemBean> loadMoreServing = parseOrderList
							.parseOrderList(loadMoreResultSucess.toString());
					if (loadMoreServing != null && loadMoreServing.size() > 0) {
						if (loadMoreServing.size() > 4) {
							order_xListView.setPullLoadEnable(true);
							order_xListView.haveOrderShow();
						} else {
							order_xListView.noOrderShow();
						}
						isLoadMore = servingOrderList.addAll(loadMoreServing);
					} else {
						order_xListView.noOrderShow();
					}
					if (isLoadMore && XListViewTag) {
						if (servingOrderList != null
								&& (orderAdapterServing != null)
								&& (orderAdapterServing.orderList != null)) {
							orderAdapterServing.orderList = servingOrderList;
							orderAdapterServing.notifyDataSetChanged();
						}
					}
				}
				break;
			case 8:
				break;
			case 9:
				String loadCompletedSucess = (String) msg.obj;
				if (loadCompletedSucess != null) {
					ArrayList<OrderListItemBean> loadMoreCompleted = parseOrderList
							.parseOrderList(loadCompletedSucess.toString());
					if (loadMoreCompleted != null
							&& loadMoreCompleted.size() > 0) {
						if (loadMoreCompleted.size() > 4) {
							order_xListView.setPullLoadEnable(true);
							order_xListView.haveOrderShow();
						} else {
							order_xListView.noOrderShow();
						}
						isLoadMoreCom = completedOrderList
								.addAll(loadMoreCompleted);
					} else {
						order_xListView.noOrderShow();
					}
					if (isLoadMoreCom && !XListViewTag) {
						if (completedOrderList != null
								&& (orderAdapterCompleted != null)
								&& (orderAdapterCompleted.orderList != null)) {
							orderAdapterCompleted.orderList = completedOrderList;
							orderAdapterCompleted.notifyDataSetChanged();
						}
					}
				}
				break;
			case 10:
				break;

			default:
				break;
			}
		};
	};

	/**
	 * @return serving order list
	 * @author wei-spring
	 */
	private void getServingOrderList() {
		if (isHasNet()) {
			pageTagServing = 2;
			if (saveUtils.getStrSP("user_id") != null) {
				orderListParams.put("user_id", saveUtils.getStrSP("user_id"));
			}
			orderListParams.put("order_type", "0");
			orderListParams.put("page", "1");
			orderListParams.put("per_page", String.valueOf(perPage));
			((MainActivity) getActivity()).getdate(orderListParams,
					Constants.getoerderlist, handler, TUREMESSAGESERVING,
					ERRORMESSAGESERVING, false, false, true);
		} else {
			noNetDo();
		}
	}

	/**
	 * @return more serving order list
	 * @author wei-spring
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getMoreServingOrderList() {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				if (isHasNet()) {
					if (saveUtils.getStrSP("user_id") != null) {
						orderListParams.put("user_id",
								saveUtils.getStrSP("user_id"));
					}
					orderListParams.put("order_type", "0");
					orderListParams.put("page", String.valueOf(pageTagServing));
					orderListParams.put("per_page", String.valueOf(perPage));
					((MainActivity) getActivity()).getdate(orderListParams,
							Constants.getoerderlist, handler,
							TUREMESSAGESERVINGMORE, ERRORMESSAGESERVINGMORE,
							false, false, false);
				} else {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							noNetDo();
						}
					});
				}
				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				pageTagServing++;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				orderListParams.clear();
			}

		}.execute();
	}

	/**
	 * @return serving order list
	 * @author wei-spring
	 */
	private void getCompletedOrderList() {
		if (isHasNet()) {
			pageTagCompleted = 2;
			if (saveUtils.getStrSP("user_id") != null) {
				orderListParams.put("user_id", saveUtils.getStrSP("user_id"));
			}
			orderListParams.put("order_type", "1");
			orderListParams.put("page", "1");
			orderListParams.put("per_page", String.valueOf(perPage));
			((MainActivity) getActivity()).getdate(orderListParams,
					Constants.getoerderlist, handler, TUREMESSAGECOMPLETED,
					ERRORMESSAGECOMPLETED, true, true, false);
		} else {
			noNetDo();
		}
	}

	/**
	 * @return more completed order list
	 * @author wei-spring
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getMoreCompletedOrderList() {
		new AsyncTask() {
			@Override
			protected Object doInBackground(Object... arg0) {
				if (isHasNet()) {
					if (saveUtils.getStrSP("user_id") != null) {
						orderListParams.put("user_id",
								saveUtils.getStrSP("user_id"));
					}
					orderListParams.put("order_type", "1");
					orderListParams.put("page",
							String.valueOf(pageTagCompleted));
					orderListParams.put("per_page", String.valueOf(perPage));
					((MainActivity) getActivity()).getdate(orderListParams,
							Constants.getoerderlist, handler, SUCESSMSGGMORE,
							FAILMSGMORE, false, false, false);
				} else {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							noNetDo();
						}
					});
				}
				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				super.onPostExecute(result);
				pageTagCompleted++;
			}

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				orderListParams.clear();
			}

		}.execute();
	}

	/**
	 * @author wei-spring
	 * 
	 */
	public void removeOrder(String order_id) {
		orderListParams.clear();
		if (saveUtils.getStrSP("user_id") != null) {
			orderListParams.put("user_id", saveUtils.getStrSP("user_id"));
		}
		if (AppConfig.getInstance().getCancleOrderString() != null) {
			orderListParams.put("reason", AppConfig.getInstance()
					.getCancleOrderString());
		}
		orderListParams.put("order_id", order_id);
		((MainActivity) getActivity()).postdate(orderListParams,
				Constants.getcancelorder, handler, TUREMESSAGEREMOVE,
				ERRORMESSAGEREMOVE, false, true);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			isShowPage = true;
			if (isHasNet()) {
				order_login_btn.setVisibility(View.GONE);
				order_login_tips.setVisibility(View.GONE);
				if (!saveUtils.getBoolSP(KeepingData.LOGINED)) {
					order_login_btn.setVisibility(View.VISIBLE);
					order_login_tips.setVisibility(View.VISIBLE);
				} else {
					if (!XListViewTag) {
						serving_btn_listener();
					} else {
						serving_btn_listener();
					}
				}
			} else {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								noNetDo();
							}
						});
					}
				}, 200);
			}
		} else {
			isShowPage = false;
		}

	}

	/**
	 * no net should done
	 */
	public void noNetDo() {
		if (!isHasNet()) {
			order_xListView.setVisibility(View.GONE);
			iv_no_order.setVisibility(View.GONE);
			iv_no_net_wififail.setVisibility(View.VISIBLE);
			iv_no_net_wififail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					iv_loading.setVisibility(View.VISIBLE);
					new Thread() {
						public void run() {
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									iv_loading.setVisibility(View.GONE);
								}
							});
						};
					}.start();
					if (XListViewTag) {
						orderAdapterServing = null;
						serving_btn_listener();
					} else {
						orderAdapterCompleted = null;
						completed_btn_listener();
					}

				}
			});
		} else {
			iv_no_net_wififail.setVisibility(View.GONE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_order, container, false);
		iv_no_order = (ImageView) view.findViewById(R.id.iv_no_order);
		iv_loading = (ImageView) view.findViewById(R.id.iv_loading);
		iv_no_net_wififail = (ImageView) view
				.findViewById(R.id.iv_no_net_wififail);
		animationDrawable = (AnimationDrawable) iv_no_order.getBackground();
		order_xListView = (XListView) view.findViewById(R.id.order_xListView);
		order_xListView.setPullLoadEnable(false);
		order_xListView.hintFooter();
		order_xListView.setXListViewListener(this);

		mHandler = new Handler();
		order_login_btn = (TextView) view.findViewById(R.id.order_login_btn);
		order_login_tips = (TextView) view.findViewById(R.id.order_login_tips);
		order_login_btn.setVisibility(View.GONE);
		order_login_tips.setVisibility(View.GONE);
		if (!saveUtils.getBoolSP(KeepingData.LOGINED)) {
			if (isHasNet()) {
				order_login_tips.setVisibility(View.VISIBLE);
				order_login_btn.setVisibility(View.VISIBLE);
			}
		}
		order_login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!saveUtils.getBoolSP(KeepingData.LOGINED)) {
					startActivity(new Intent(getActivity(), LoginActivity.class));
					return;
				}
			}
		});
		serving_btn = (RadioButton) view.findViewById(R.id.serving_btn);
		completed_btn = (RadioButton) view.findViewById(R.id.completed_btn);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						serving_btn_listener();
					}
				});
			}
		}, 500);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		serving_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				serving_btn_listener();
			}
		});
		completed_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				completed_btn_listener();
			}
		});

	}

	/**
	 * button for listener serving order list
	 * 
	 * @param view
	 */
	public void serving_btn_listener() {
		XListViewTag = true;
		completed_btn.setChecked(false);
		serving_btn.setChecked(true);
		serving_btn.setBackgroundResource(R.drawable.left_selected);
		serving_btn.setTextColor(getResources().getColor(R.color.dark_blue));
		completed_btn.setBackgroundResource(R.drawable.right_default);
		completed_btn.setTextColor(getResources().getColor(R.color.white));
		if (isHasNet()) {
			iv_no_order.setVisibility(View.GONE);
			iv_loading.setVisibility(View.GONE);
			iv_no_net_wififail.setVisibility(View.GONE);
			order_xListView.setVisibility(View.VISIBLE);
			if (orderAdapterServing != null) {
				orderAdapterServing.notifyDataSetChanged();
				orderAdapterServing = null;
			}
			if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
				getServingOrderList();
			}
		} else {
			noNetDo();
		}
	}

	/**
	 * button for listener completed order list
	 * 
	 * @param view
	 */
	public void completed_btn_listener() {
		XListViewTag = false;
		serving_btn.setChecked(false);
		completed_btn.setChecked(true);
		serving_btn.setBackgroundResource(R.drawable.left_default);
		serving_btn.setTextColor(getResources().getColor(R.color.white));
		completed_btn.setBackgroundResource(R.drawable.right_selected);
		completed_btn.setTextColor(getResources().getColor(R.color.dark_blue));
		if (isHasNet()) {
			iv_no_order.setVisibility(View.GONE);
			iv_loading.setVisibility(View.GONE);
			iv_no_net_wififail.setVisibility(View.GONE);
			order_xListView.setVisibility(View.VISIBLE);
			if (orderAdapterCompleted != null) {
				orderAdapterCompleted.notifyDataSetChanged();
				orderAdapterCompleted = null;
			}
			if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
				getCompletedOrderList();
			}
		} else {
			noNetDo();
		}
	}

	private void onRefreshNow() {
		order_xListView.stopRefresh();
		order_xListView.stopLoadMore();

		if (XListViewTag) {
			orderAdapterServing = null;
			if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
				getServingOrderList();
			}
		} else {
			orderAdapterCompleted = null;
			if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
				getCompletedOrderList();
			}
		}
	}

	private void onLoadMoreNow() {
		order_xListView.stopRefresh();
		order_xListView.stopLoadMore();

		if (XListViewTag) {
			getMoreServingOrderList();
		} else {
			getMoreCompletedOrderList();
		}
	}

	/**
	 * @author wei-spring
	 * @return refresh listview
	 */
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onRefreshNow();
			}
		}, 2000);

	}

	/**
	 * @author wei-spring
	 * @return load more
	 */
	@Override
	public void onLoadMore() {

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				onLoadMoreNow();
			}
		}, 2000);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		saveUtils = new SaveUtils(mActivity);
	}

	/***
	 * judge Internet is available
	 * 
	 * @author wei-spring
	 * @return
	 */
	public boolean isHasNet() {
		ConnectivityManager cwjManager = (ConnectivityManager) mActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager.getActiveNetworkInfo() != null) {
			return cwjManager.getActiveNetworkInfo().isAvailable();
		} else {
			return false;
		}
	}

	/**
	 * get order id from order item by event bus
	 */
	public void onEvent(OrderListAdapterEvent event) {
		switch (event.getText()) {
		case "RefeshServingOrderList":
		case "ShanChuDingDan":
		case "OrderSucess":
			orderAdapterServing = null;
			serving_btn_listener();
			pageTagServing = 2;
			break;
		case "RefeshCompletedOrderList":
		case "PingJiaSucess":
			orderAdapterCompleted = null;
			completed_btn_listener();
			break;
		case "ZhiFuBaoPaySucess":
		case "XianJinPaySucess":
		case "Payinterrupt":
			System.out.println("订单状态刷新");
			new Handler().postDelayed(new Runnable() {
				public void run() {
					orderAdapterServing = null;
					serving_btn_listener();
					pageTagServing = 2;
				}
			}, 1000);
			break;
		case "YuEPaySucess":
		case "WXPaySucess":
			((MainActivity) getActivity()).showdialog("支付成功！");
			orderAdapterServing = null;
			serving_btn_listener();
			pageTagServing = 2;
			break;
		case "NoNetQuXiaoDingDan":
			noNetDo();
			break;
		case "WxHuiDiaoSucess":
			getOrderIsShare();
			break;
		default:
			if (event.getText().matches("[0-9]*")) {
				removeOrder(event.getText());
			}
			break;
		}
	}

	public boolean isShowShareDialog() {
		for (OrderListItemBean order_list_element : servingOrderList) {
			if ((order_list_element.getOrder_can_share().matches("[0-9]*"))) {
				int order_share_total = Integer.parseInt(order_list_element
						.getOrder_can_share());
				if (order_share_total > 0) {
					share_coupon_total = order_list_element
							.getOrder_can_share();
					share_order_url = order_list_element.getShare_url();
					share_title = order_list_element.getShare_title();
					share_content = order_list_element.getShare_content();
					share_image_url = order_list_element.getShare_image_url();
					appConfig
							.setmShareOrderId(order_list_element.getOrder_id());
					isShowSharedialog = true;
					return isShowSharedialog;
				}
			}
		}
		return isShowSharedialog;
	}

	public void getOrderIsShare() {
		sharearm.clear();
		if (appConfig.getmShareOrderId() != null) {
			sharearm.put("order_id", appConfig.getmShareOrderId());
		} else {
			return;
		}
		((MainActivity) getActivity()).postdate(sharearm,
				Constants.getorderisshare, mHandler, GETISORDERSHARESUCESS,
				GETISORDERSHAREFAIL, true, false);

	}
}
