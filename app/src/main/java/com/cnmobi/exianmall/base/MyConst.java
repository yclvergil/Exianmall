package com.cnmobi.exianmall.base;

import com.cnmobi.exianmall.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * 常量类
 * 
 */
public class MyConst {

	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions IM_IMAGE_OPTIONS = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.iv_imageloader_failure) // 设置图片下载期间显示的图片
			.showImageForEmptyUri(R.drawable.iv_imageloader_failure) // 设置图片Uri为空或是错误的时候显示的图片
			.showImageOnFail(R.drawable.iv_imageloader_failure) // 设置图片加载或解码过程中发生错误显示的图片
			.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
			.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
			.displayer(new RoundedBitmapDisplayer(0)) // 设置成圆角图片
			.displayer(new SimpleBitmapDisplayer())
//			.displayer(new FadeInBitmapDisplayer(500))// 是否图片加载好后渐入的动画时间  加这个存在刷新adapter的时候图片闪烁问题。设置下面那行的属性就好了
			.displayer(new SimpleBitmapDisplayer())
			.build(); // 创建配置过得DisplayImageOption对象
	
	/**
	 * 加载验证码图片时使用  
	 */
	public static DisplayImageOptions IM_IMAGE_OPTIONS_NO_CACHE = new DisplayImageOptions.Builder()
	.showStubImage(R.drawable.iv_imageloader_failure) // 设置图片下载期间显示的图片
	.showImageForEmptyUri(R.drawable.iv_imageloader_failure) // 设置图片Uri为空或是错误的时候显示的图片
	.showImageOnFail(R.drawable.iv_imageloader_failure) // 设置图片加载或解码过程中发生错误显示的图片
	.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
	.cacheOnDisc(false) // 设置下载的图片是否缓存在SD卡中
	.displayer(new RoundedBitmapDisplayer(0)) // 设置成圆角图片
	.displayer(new SimpleBitmapDisplayer())
//	.displayer(new FadeInBitmapDisplayer(500))// 是否图片加载好后渐入的动画时间  加这个存在刷新adapter的时候图片闪烁问题
	.build(); // 创建配置过得DisplayImageOption对象

	public static final String Baseurl = "http://121.46.0.219:8080/efreshapp";//服务器       
//	public static final String Baseurl = "http://192.168.0.116:8080/efreshapp";//吴桐
//	public static final String Baseurl = "http://121.46.0.219:8088/efreshapp";//测试服务器
//	public static final String Baseurl = "http://192.168.0.105:8080/efreshapp";//刘积训

	
	/** 搜索历史 */   
	public static String serachSp = "serach";
	/** 留言历史 */
	public static String onlineMsgSp = "onlineMsg";  
	/**
	 * 是否登录
	 */
	public static String isLogin = "isLogin";
	/**
	 * 用户名
	 */
	public static String userName = "userName";
	/**
	 * 密码
	 */
	public static String userPwd = "userPwd";

	/**
	 * 首页轮播
	 */
	public static String carouselAction = Baseurl+"/carouselAction";
	/**
	 * 所有分类
	 */
	public static String allCategoryAction = Baseurl+"/allCategoryAction";
	/**
	 * 买家首页历史订单
	 */
	public static String historicalAction = Baseurl+"/historicalAction";
	/**
	 * 登录
	 */
	public static String userLoginAction = Baseurl+"/userLoginAction";
	/**
	 * 修改登录密码
	 */
	public static String updatePassWordAction = Baseurl+"/updatePassWordAction";
	/**
	 * 注册前验证手机号是否存在 post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/testefreshapp/verifyAction?phone=18071173689
	 */
	public static String verifyAction = Baseurl+"/verifyAction";
	/**
	 * 注册获取短信验证码接口 post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/sendCodeAction?phone=15920916679
	 */
	public static String sendCodeAction = Baseurl+"/sendCodeAction";
	/**
	 * 注册接口 post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/testefreshapp/registerAction?type=0&phone
	 * =13250747905&password=123456&code=077204&confimPassword=123456
	 * &province=广东省&city=广州市&area=天河区&address=珠江新城54号&agree=0
	 */
	public static String registerAction = Baseurl+"/registerAction";
	/**
	 * 买家首页商品列表
	 */
	public static String commodityListAction = Baseurl+"/commodityListAction";
	/**
	 * 搜索商品 http://121.46.0.219:8080/testefreshapp/testefreshapp/searchCommodity?idStore=1001&
	 * idCategory=1001&idCompany=1000&commodityname=白菜&price=asc&currentPage=1
	 */
	public static String searchCommodity = Baseurl+"/searchCommodity";
	/**
	 * 查询购物车接口 post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/shoppCartAction?idUser=1000
	 */
	public static String shoppCartAction = Baseurl+"/shoppCartAction";
	/**
	 * 商品详情
	 */
	public static String commodityDetailsAction = Baseurl+"/commodityDetailsAction?";
	/**
	 * 商品评价查询
	 * http://121.46.0.219:8080/testefreshapp/commodityEvaluateAction?idLevel=11&currentPage=1&idCommodity=11&idUser=1202&token=
	 */
	public static String commodityEvaluateAction = Baseurl+"/commodityEvaluateAction?";
	/**
	 * 商品等级切换
	 */
	public static String cutLevelAction = Baseurl+"/cutLevelAction?";
	/**
	 * 商品产地查询 请求方式：get
	 */
	public static String commodityOriginAction = Baseurl+"/commodityOriginAction";
	/**
	 * 商品规格查询 请求方式：get
	 */
	public static String commodityNormsAction = Baseurl+"/commodityNormsAction";
	/**
	 * 添加至购物车
	 */
	public static String addShoppCartAction = Baseurl+"/addShoppCartAction?";
	/**
	 * 删除购物车商品 post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/delShoppCartAction?idShoppCart=1001
	 */
	public static String delShoppCartAction = Baseurl+"/delShoppCartAction";
	/**
	 * 修改商品购买数量接口 post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/modifyBuyNumber?idShoppCart=1002
	 */
	public static String modifyBuyNumber = Baseurl+"/modifyBuyNumber";
	/**
	 * 卖家产地风采查询
	 * 地址：http://121.46.0.219:8080/testefreshapp/presenceOfOriginAction?idStore=1001
	 */
	public static String presenceOfOriginAction = Baseurl+"/presenceOfOriginAction";
	/**
	 * 确认订单 地址：http://121.46.0.219:8080/testefreshapp/confirmOrderAction?idUser=1000
	 */
	public static String confirmOrderAction = Baseurl+"/confirmOrderAction?";
	/**
	 * 所有订单 post请求
	 * http://121.46.0.219:8080/testefreshapp/allOrderAction?isCut=0&idUser=1000
	 * isCut:订单状态 0:待付款 1:待收货 2:待评分
	 */
	public static String allOrderAction = Baseurl+"/allOrderAction?";
	/**
	 * 提交订单 post请求
	 * 地址：地址：http://121.46.0.219:8080/testefreshapp/submitOrderAction?orderInfo=
	 * orderInfo：订单信息 参数格式如下： [{ "idStore": 1001, //店铺主键"idUser": 1000,
	 * //登录人主键"creationordertime": "2016-01-28 14:17:23",
	 * //下单时间，取系统时间"arrivaltime": "2016-01-28 14:17:23", //要求到货时间"orderprice":
	 * 180, //订单总价"orderdetailed": [{ //订单商品列表"buynumber": 1, //购买数量"buyprice":
	 * 150, //单价"idCommodity": 1000, //商品主键"idLevel": 1000//级别主键 }] }, {
	 * "idStore": 1001, //店铺主键"idUser": 1000, //登录人主键"creationordertime":
	 * "2016-01-28 14:17:23", //下单时间，取系统时间"arrivaltime": "2016-01-28 14:17:23",
	 * //要求到货时间"orderprice": 180, //订单总价"orderdetailed": [{ //订单商品列表"buynumber":
	 * 1, //购买数量"buyprice": 150, //单价"idCommodity": 1000, //商品主键"idLevel":
	 * 1000//级别主键 }] }]
	 */
	public static String submitOrderAction = Baseurl+"/submitOrderAction?";
	/**
	 * 积分明细 请求方式 get
	 * 地址：http://121.46.0.219:8080/testefreshapp/integralSubsidiaryAction
	 * ?idUser=1000&currentPage=1
	 */
	public static String integralSubsidiaryAction = Baseurl+"/integralSubsidiaryAction?";

	/**
	 * 我的钱包-预充值规则接口 post 请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/rechargeruleAction?idUser=1000
	 */
	public static String rechargeruleAction = Baseurl+"/rechargeruleAction?";
	/**
	 * 用户反馈：post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/userFeedbackAction?idFeedback
	 * =1000&feedcontent=这是反馈内容！
	 */
	public static String userFeedbackAction = Baseurl+"/userFeedbackAction?";
	/**
	 * 用户评分：post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/userScoreAction?idUser=
	 * 1007&orderNo=1007&score=5&commodity=小白菜&idLevel=1008&idcommodity=1022
	 */
	public static String userScoreAction = Baseurl+"/userScoreAction?";
	/**
	 * 订单评价:post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/userEvaluateAction?idUser
	 * =1007&orderNo=1007&content=这是用户对此次订单的评价。
	 */
	public static String userEvaluateAction = Baseurl+"/userEvaluateAction?";
	/**
	 * 系统消息详情 get请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/systemMessageDetailAction
	 * ?idMessage=1000&msgtype=1&idUser=1000
	 */
	public static String systemMessageDetailAction = Baseurl+"/systemMessageDetailAction?";
	/**
	 * 忘记密码 post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/forgetPassWordAction?checkcode
	 * =&idUser=1007&phone=18802093449&newpwd=123456&okpwd=123456
	 */
	public static String forgetPassWordAction = Baseurl+"/forgetPassWordAction?";
	/**
	 * 二维码扫描收货（确认收货）
	 * 地址：http://121.46.0.219:8080/testefreshapp/deliveryAction?secondlevelorderNo
	 * =20160218035144000002&idUser=1000
	 */
	public static String confirmGoodsAction = Baseurl+"/deliveryAction?";
	/**
	 * 积分规则：post请求 地址：http://121.46.0.219:8080/testefreshapp/integralRuleAction
	 */
	public static String integralRuleAction = Baseurl+"/integralRuleAction";
	/**
	 * 留言咨询 get请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/advisoryAction?idAdvisory
	 * =1007&advcontent=I LOVE MYLOVE
	 */
	public static String advisoryAction = Baseurl+"/advisoryAction?";
	/**
	 * 收支明细 get请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/paymentDetailAction?idUser
	 * =1007&currentPage=1
	 */
	public static String paymentDetailAction = Baseurl+"/paymentDetailAction?";
	/**
	 * 充值接口 post请求 支付宝支付返回成功支付账单后调用此接口
	 * 地址：http://121.46.0.219:8080/testefreshapp/rechargeAction
	 * ?idUser=1000&idAccount
	 * =100002&amount=1000&idPayway=1000&tradingtype=0&tradingtime=2016-01-27
	 * 12:23:20&tradingNo=201602210001&note=充值
	 */
	public static String rechargeAction = Baseurl+"/rechargeAction?";
	/**
	 * 红包返利 post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/rebateRedAction?rechargeamount
	 * =1000&idUser=1000&idAccount=100002&idTradingrecords=1
	 */
	public static String rebateRedAction = Baseurl+"/rebateRedAction?";
	/**
	 * 历史订单：get请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/historicalOrderAction?idUser
	 * =1000&currentPage=1
	 */
	public static String historicalOrderAction = Baseurl+"/historicalOrderAction?";

	/**
	 * 收益报表
	 */
	public static String earningsReportAction = Baseurl+"/earningsReportAction";
	/**
	 * 关于e鲜 地址：http://121.46.0.219:8080/testefreshapp/aboutEfreshAction
	 */
	public static String aboutEfreshAction = Baseurl+"/aboutEfreshAction";
	/**
	 * 修改支付密码 post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/updatePaymentPwdAction
	 * ?idUser=1007&oldPayPwd=123456&newPayPwd=123456&confirmPayPwd=123456
	 */
	public static String updatePaymentPwdAction = Baseurl+"/updatePaymentPwdAction?";
	/**
	 * 设置支付密码 post请求 地址：
	 * http://121.46.0.219:8080/testefreshapp/setUpPayPwdAction?idUser
	 * =1007&payPwd=123456&confirmPayPwd=123456
	 */
	public static String setUpPayPwdAction = Baseurl+"/setUpPayPwdAction?";
	/**
	 * 完善我的资料（买家）：post请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/perfectBuyersInformationAction
	 * ?idUser
	 * =1007&stallsname=武汉江夏区菜市场八号档口&name=wendy&phone=18802093449&imageUrl=
	 */
	public static String perfectBuyersInformationAction = Baseurl+"/perfectBuyersInformationAction?";
	/**
	 * 价格日历
	 * 地址：http://121.46.0.219:8080/testefreshapp/calendarPriceAction?idCommodity
	 * =1000&idLevel=1000&buyDate=2016-01-08
	 */
	public static String calendarPriceAction = Baseurl+"/calendarPriceAction?";

	/**
	 * 查看卖家信息
	 */
	public static String sellerInformationQueryAction = Baseurl+"/sellerInformationQueryAction?";

	/**
	 * 上传接口 imgType的值：0=images/users/logo，1=images/users/stroe，2=意见反馈
	 */
	public static String uploadAction = Baseurl+"/uploadAction?";

	/**
	 * 保存卖家信息
	 */
	public static String sellerMsgAction = Baseurl+"/sellerMsgAction";

	/**
	 * 修改头像 userId:用户主键 userType:用户类型0=买家、1=卖家 imageName:用户头像图片
	 */
	public static String modifyHeadPortraitAction = Baseurl+"/modifyHeadPortraitAction";
	/**
	 * 系统消息列表 get请求
	 * 地址：http://121.46.0.219:8080/testefreshapp/messageListAction?idUser=1000
	 */
	public static String messageListAction = Baseurl+"/messageListAction?";
	/**
	 * 卖家订单查询 地址：http://121.46.0.219:8080/testefreshapp/sellerOrderAction?idUser=2&
	 * currentPage=1&isCut=0
	 */
	public static String sellerOrderAction = Baseurl+"/sellerOrderAction?";
	/**
	 * 卖家产地地址查询
	 * 地址：http://121.46.0.219:8080/testefreshapp/sellerAddressAction?idUser=2
	 */
	public static String sellerAddressAction = Baseurl+"/sellerAddressAction?";
	/**
	 * 卖家申请修改产地地址
	 * 地址：http://121.46.0.219:8080/testefreshapp/upAddressAction?addressup
	 * =浙江省宁波市堇州区风格雅事&provinceup=浙江省&cityup=宁波市&areaup=堇州区&idUser=2
	 */
	public static String upAddressAction = Baseurl+"/upAddressAction?";
	/**
	 * 买家收货地址查询
	 * 地址：http://121.46.0.219:8080/testefreshapp/queryProfileAction?idUser=1000
	 */
	public static String queryProfileAction = Baseurl+"/queryProfileAction?";
	/**
	 * 买家申请修改收货地址
	 * 地址：http://121.46.0.219:8080/testefreshapp/upDeliveryAddressAction?idUser
	 * =1000&
	 * provinceup=湖北省&cityup=鄂州市&areaup=鄂城区&deliveryaddress2=湖北省鄂州市鄂城区泽林镇福盛花园
	 */
	public static String upDeliveryAddressAction = Baseurl+"/upDeliveryAddressAction?";
	/**
	 * 删除用户消息
	 * 地址：http://121.46.0.219:8080/testefreshapp/deletMessageAction?idMessage=
	 * 1&msgtype=1 idMessage:消息主键(留言咨询主键，用户反馈主键，用户评价表主键)msgtype:消息类型
	 * 1=客服回复用户留言消息；2=客服回复用户反馈消息、；3=客服回复用户评价消息
	 */
	public static String deletMessageAction = Baseurl+"/deletMessageAction?";
	/**
	 * 清空用户消息
	 * 地址：http://121.46.0.219:8080/testefreshapp/emptyMessageAction?idUser=1007
	 */
	public static String emptyMessageAction = Baseurl+"/emptyMessageAction?";
	/**
	 * 卖家确认接单 地址：http://121.46.0.219:8080/testefreshapp/receiveOrderAction?
	 * secondlevelorderNo=20160201110455000002
	 */
	public static String receiveOrderAction = Baseurl+"/receiveOrderAction?";
	/**
	 * 支付订单，支付成功后修改订单的状态接口
	 * 地址：http://121.46.0.219:8080/testefreshapp/paymentOrderAction
	 * ?idOrder=&paytime= idOrder：格式如下 [{ "idOrder": "1007" }] paytime：支付的时间
	 */
	public static String paymentOrderAction = Baseurl+"/paymentOrderAction?";
	/**
	 * 某类别商品子商品查询（一级分类查询）
	 * 地址：http://1121.46.0.219:8080/testefreshapp/categoryListAction?idCategory=1001&currentPage=1&idUser=&toKen=
	 */
	public static String commiditySortAction = Baseurl+"/categoryListAction?";
	/**
	 * 根据商品名称和商品类别查询（二级分类查询）
	 * 地址：http://121.46.0.219:8080/testefreshapp/categoryListAction
	 * ?idCategory=1001&commodityName=小白菜
	 */
	public static String categoryListAction = Baseurl+"/categoryListAction?";
	/**
	 * 平台支付密码验证
	 * 地址：http://121.46.0.219:8080/testefreshapp/passwordValidationAction?idUser
	 * =1007&paymentpwd=123456
	 */
	public static String passwordValidationAction = Baseurl+"/passwordValidationAction?";

	/**
	 * 热门搜索
	 */
	public static String hotSearchAction = Baseurl+"/hotSearchAction";
	/**
	 * 余额支付
	 *  地址： http://121.46.0.219:8080/testefreshapp/balancePaidAction?idUser=1000& payPrice=1000.99&idOrder= 
	 *  [{"idOrder": "1490"},{"idOrder":"1491"},{"idOrder": "1492"}]
	 *  &paytime=2016-03-21 12:00:00 payPrice:交易金额
	 *  idUser:用户主键 idOrder:订单 paytime:交易时间
	 */
	public static String balancePaidAction=Baseurl+"/balancePaidAction?";
	/**
	 * 退出登录接口
	 */
	public static String userLogoutAction=Baseurl+"/userLogoutAction";
	/**
	 * 历史订单 修改商品评价接口
	 * 地址：http://121.46.0.219:8080/testefreshapp/updateEvaluateAction?idScore=168&evaluatecontent=修改评价啦&token=&idUser=120
	 */
	public static String updateEvaluateAction=Baseurl+"/updateEvaluateAction?";
	/**
	 * 申请退款接口   post提交
	 * 地址:http://121.46.0.219:8080/testefreshapp/refundApplyAction?idOrder=11&orderNo=343w32&idUser=11&idCommodity=11&idLevel=11&price=11&applydate=2016-04-16
	 */
	public static String refundApplyAction=Baseurl+"/refundApplyAction?";
	/**
	 * 图形验证码地址
	 */
	public static String imageVerifyAction=Baseurl+"/imageVerifyAction?";
	/**
	 * 验证图形验证码
	 * 地址：http://www.1efresh.com//efreshapp/validateImageCodeAction?phone=18802093449&imageCode=A6BZ
	 */
	public static String validateImageCodeAction=Baseurl+"/validateImageCodeAction?";
	/**
	 * 保存充值信息
	 * http://www.1efresh.com/efreshapp/rechargeRecordAction?idUser=1202&phone=18802093449&rechargeamount=88.88&tradingNo=1234567890&payway=0&rechargetime=2016-04-28 00:00:00
	 */
	public static String rechargeRecordAction=Baseurl+"/rechargeRecordAction?";
	/**
	 * 版本更新接口
	 */
	public static String newVersion=Baseurl+"/newVersion?";
	/**
	 * 买家取消订单接口
	 */
	public static String cancelOrder=Baseurl+"/cancelOrder?";
	
}
