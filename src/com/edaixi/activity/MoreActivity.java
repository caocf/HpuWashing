package com.edaixi.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.edaixi.Enum.WebPageType;
import com.edaixi.data.AppConfig;
import com.edaixi.data.KeepingData;
import com.edaixi.util.SaveUtils;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class MoreActivity extends BaseActivity {
	private ListView more_list;
	final String str[] = { "欢迎页面", "常见问题", "给我们评分", "联系我们", "版本升级", "用户协议",
			"关于e袋洗", "退出账号" };
	final int icon[] = { R.drawable.welcome, R.drawable.problem,
			R.drawable.score, R.drawable.contact, R.drawable.upgrade,
			R.drawable.agreement, R.drawable.about, R.drawable.signout };
	private Mapter adapter;
	private ImageView more_back_btn;
	private SaveUtils saveUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more);
		init(this);
		saveUtils = new SaveUtils(this);
		more_back_btn = (ImageView) findViewById(R.id.more_back_btn);
		more_back_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackKeyDown();
			}
		});
		more_list = (ListView) findViewById(R.id.more_list);
		adapter = new Mapter();
		more_list.setAdapter(adapter);
	}

	public void onResume() {
		super.onResume();
		TCAgent.onResume(this);
		MobclickAgent.onPageStart("MoreActivity"); // 统计页面
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		TCAgent.onPause(this);
		MobclickAgent.onPageEnd("MoreActivity"); // 保证 onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	protected boolean onBackKeyDown() {
		finish(0, 0);
		return true;
	}

	private class Mapter extends BaseAdapter {
		private Viewholer holder;

		@Override
		public int getCount() {
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			holder = new Viewholer();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = (RelativeLayout) inflater.inflate(
					R.layout.more_list_item, null);
			holder.imag = (ImageView) convertView.findViewById(R.id.imag);
			holder.iv_divide_line = (ImageView) convertView
					.findViewById(R.id.iv_divide_line);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.to_right_arrow = (ImageView) convertView
					.findViewById(R.id.to_right_arrow);
			holder.deviid_line = convertView.findViewById(R.id.deviid_line);
			holder.imag.setImageResource(icon[position]);
			holder.text.setText(str[position]);
			if (position == 4) {
				holder.iv_divide_line.setVisibility(View.VISIBLE);
				holder.deviid_line.setVisibility(View.INVISIBLE);
			} else if (position == 7) {
				holder.deviid_line.setVisibility(View.INVISIBLE);
				holder.iv_divide_line.setVisibility(View.VISIBLE);
			} else if (position == 8) {
				holder.deviid_line.setVisibility(View.INVISIBLE);
				holder.to_right_arrow.setVisibility(View.INVISIBLE);
			} else {
				holder.deviid_line.setVisibility(View.VISIBLE);
				holder.iv_divide_line.setVisibility(View.GONE);
			}
			final Intent intent = new Intent();
			final Bundle mBundle = new Bundle();
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (position) {
					case 0:
						mBundle.putInt("FLAG", 1);
						intent.putExtras(mBundle);
						intent.setClass(MoreActivity.this, SplashActivity.class);
						startActivity(intent);
						break;
					case 1:
						TCAgent.onEvent(getActivity(), "更多_常见问题");
						mBundle.putInt("TYPE",
								WebPageType.COMMON_ISSUE.getType());
						intent.putExtras(mBundle);
						intent.setClass(MoreActivity.this, WebActivity.class);
						startActivity(intent);
						break;
					case 2:
						Uri uri;
						if (hasAnyMarketInstalled(getContext())) {
							uri = Uri.parse("market://details?id="
									+ getPackageName());
						} else {
							Toast.makeText(getApplicationContext(),
									"您没有安装应用市场", Toast.LENGTH_SHORT).show();
							return;
						}
						// uri = Uri.parse("http://www.edaixi.com");
						intent.setData(uri);
						intent.setAction(Intent.ACTION_VIEW);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						break;
					case 3:
						TCAgent.onEvent(getActivity(), "更多_服务热线");
						AlertDialog.Builder dialog = new Builder(getContext());
						dialog.setMessage("拨打客服电话\n400-818-7171?");
						dialog.setPositiveButton("确定",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// intent.setAction(Intent.ACTION_CALL);
										intent.setAction(Intent.ACTION_DIAL);
										intent.setData(Uri.parse("tel:"
												+ "4008187171"));
										startActivity(intent);
									}
								}).setNegativeButton("取消", null);
						dialog.show();
						break;
					case 4:
						// 检测版本
						System.out.println("dianji");
						checkversion();
						break;
					case 5:
						TCAgent.onEvent(getActivity(), "更多_用户协议");
						mBundle.putInt("TYPE",
								WebPageType.USERPRTOCOL.getType());
						intent.putExtras(mBundle);
						intent.setClass(MoreActivity.this, WebActivity.class);
						startActivity(intent);
						break;
					case 6:
						startActivity(new Intent(getContext(),
								AboutUsActivity.class));
						break;

					case 7:
						if (saveUtils.getBoolSP(KeepingData.LOGINED)) {
							AlertDialog.Builder eixtdialog = new Builder(
									getContext());
							eixtdialog.setMessage("确定退出当前账户？");
							eixtdialog.setPositiveButton("确定",
									new AlertDialog.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											AppConfig.getInstance().setIslogin(
													false);
											getsaveutils_instants().saveBoolSP(
													KeepingData.LOGINED, false);
											getsaveutils_instants()
													.saveBoolSP(
															KeepingData.IS_FIRSTLOGINED,
															false);
											Intent intent = new Intent(
													getContext(),
													MainActivity.class);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											startActivity(intent);
										}
									}).setNegativeButton("取消", null);
							eixtdialog.show();
						} else {
							Toast.makeText(MoreActivity.this, "您还没有登录，请先登录.",
									Toast.LENGTH_SHORT).show();
						}
						break;
					default:

						break;
					}
				}
			});
			return convertView;
		}

		protected void checkversion() {
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus,
						UpdateResponse updateResponse) {
					switch (updateStatus) {
					case 0:
						break;
					case 1:
						showdialog("当前版本已是最新版本");
						break;
					case 3:
						showdialog("网络不给力");
						break;
					default:
						break;
					}
				}
			});
			UmengUpdateAgent.forceUpdate(getActivity());
		}

		class Viewholer {
			ImageView imag;
			ImageView iv_divide_line;
			TextView text;
			ImageView to_right_arrow;
			View deviid_line;
		}
	}

	public static boolean hasAnyMarketInstalled(Context context) {
		Intent intent = new Intent();
		intent.setData(Uri.parse("market://details?id=android.browser"));
		List<ResolveInfo> list = context.getPackageManager()
				.queryIntentActivities(intent,
						PackageManager.MATCH_DEFAULT_ONLY);
		return 0 != list.size();
	}
}
