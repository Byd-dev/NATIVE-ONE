package com.pro.bityard.config;

public class AppConfig {
    public static final String S_KEY = "1111111122222222";

    public static final long QUOTE_SECOND = 2000;
    public static final long ITEM_QUOTE_SECOND = 1000;

    public static String SIGN_KEY = "Hello, dear friends! Welcome to bityard!";
    public static final String FIRST = "first";
    public static final String LOAD = "load";
    public static final String TRADE = "TRADE";
    public static final String FOLLOW = "FOLLOW";


    //后台返回商品分区
    public static final String TYPE_FT = "FT";//合约
    public static final String TYPE_CH = "CH";//现货
    public static final String TYPE_FE = "FE";//现货

    public static final String ZONE_MAIN = "main";//合约主区
    public static final String ZONE_INNOVATION = "inno";//合约创新
    public static final String ZONE_DERIVATIVES = "deriv";//合约衍生
    public static final String ZONE_DEFI = "defi";//现货
    public static final String ZONE_POS = "pos";//现货
    //默认选中那个位置
    public static final int selectPosition=1;

    //页面分区
    public static final String VIEW_OPTIONAL = "VIEW_OPTIONAL";//自选
    public static final String VIEW_OPTIONAL_SPOT = "VIEW_OPTIONAL_SPOT";//自选
    public static final String VIEW_OPTIONAL_CONTRACT = "VIEW_OPTIONAL_CONTRACT";//自选
    public static final String VIEW_OPTIONAL_DERIVATIVES = "VIEW_OPTIONAL_DERIVATIVES";//自选

    public static final String VIEW_SPOT = "VIEW_SPOT";//现货
    public static final String VIEW_CONTRACT = "VIEW_CONTRACT";//合约主区
    public static final String VIEW_DERIVATIVES = "VIEW_DERIVATIVES";//合约创新
    public static final String VIEW_FOREIGN_EXCHANGE = "VIEW_FOREIGN_EXCHANGE";//合约创新
    public static final String VIEW_SPOT_DEFI = "VIEW_SPOT_DEFI";//现货defi
    public static final String VIEW_SPOT_POS = "VIEW_SPOT_POS";//现货defi


    //自选列表
    public static final String OPTIONAL_ALL = "OPTIONAL_ALL";
    //自选 价格高2低
    public static final String OPTIONAL_PRICE_HIGH2LOW = "OPTIONAL_PRICE_HIGH2LOW";
    //自选 价格低2高
    public static final String OPTIONAL_PRICE_LOW2HIGH = "OPTIONAL_PRICE_LOW2HIGH";
    //自选 涨跌幅高2低
    public static final String OPTIONAL_RATE_HIGH2LOW = "OPTIONAL_RATE_HIGH2LOW";
    //自选 涨跌幅低2高
    public static final String OPTIONAL_RATE_LOW2HIGH = "OPTIONAL_RATE_LOW2HIGH";
    //自选 名称A到Z
    public static final String OPTIONAL_NAME_A2Z = "OPTIONAL_NAME_A2Z";
    //自选 名称Z到A
    public static final String OPTIONAL_NAME_Z2A = "OPTIONAL_NAME_Z2A";


    //现货自选列表
    public static final String OPTIONAL_SPOT_ALL = "OPTIONAL_SPOT_ALL";
    //自选 价格高2低
    public static final String OPTIONAL_SPOT_PRICE_HIGH2LOW = "OPTIONAL_SPOT_PRICE_HIGH2LOW";
    //自选 价格低2高
    public static final String OPTIONAL_SPOT_PRICE_LOW2HIGH = "OPTIONAL_SPOT_PRICE_LOW2HIGH";
    //自选 涨跌幅高2低
    public static final String OPTIONAL_SPOT_RATE_HIGH2LOW = "OPTIONAL_SPOT_RATE_HIGH2LOW";
    //自选 涨跌幅低2高
    public static final String OPTIONAL_SPOT_RATE_LOW2HIGH = "OPTIONAL_SPOT_RATE_LOW2HIGH";
    //自选 名称A到Z
    public static final String OPTIONAL_SPOT_NAME_A2Z = "OPTIONAL_SPOT_NAME_A2Z";
    //自选 名称Z到A
    public static final String OPTIONAL_SPOT_NAME_Z2A = "OPTIONAL_SPOT_NAME_Z2A";

    //合约自选列表
    public static final String OPTIONAL_CONTRACT_ALL = "OPTIONAL_CONTRACT_ALL";
    //自选 价格高2低
    public static final String OPTIONAL_CONTRACT_PRICE_HIGH2LOW = "OPTIONAL_CONTRACT_PRICE_HIGH2LOW";
    //自选 价格低2高
    public static final String OPTIONAL_CONTRACT_PRICE_LOW2HIGH = "OPTIONAL_CONTRACT_PRICE_LOW2HIGH";
    //自选 涨跌幅高2低
    public static final String OPTIONAL_CONTRACT_RATE_HIGH2LOW = "OPTIONAL_CONTRACT_RATE_HIGH2LOW";
    //自选 涨跌幅低2高
    public static final String OPTIONAL_CONTRACT_RATE_LOW2HIGH = "OPTIONAL_CONTRACT_RATE_LOW2HIGH";
    //自选 名称A到Z
    public static final String OPTIONAL_CONTRACT_NAME_A2Z = "OPTIONAL_CONTRACT_NAME_A2Z";
    //自选 名称Z到A
    public static final String OPTIONAL_CONTRACT_NAME_Z2A = "OPTIONAL_CONTRACT_NAME_Z2A";


    //衍生品自选列表
    public static final String OPTIONAL_DERIVATIVES_ALL = "OPTIONAL_DERIVATIVES_ALL";
    //自选 价格高2低
    public static final String OPTIONAL_DERIVATIVES_PRICE_HIGH2LOW = "OPTIONAL_DERIVATIVES_PRICE_HIGH2LOW";
    //自选 价格低2高
    public static final String OPTIONAL_DERIVATIVES_PRICE_LOW2HIGH = "OPTIONAL_DERIVATIVES_PRICE_LOW2HIGH";
    //自选 涨跌幅高2低
    public static final String OPTIONAL_DERIVATIVES_RATE_HIGH2LOW = "OPTIONAL_DERIVATIVES_RATE_HIGH2LOW";
    //自选 涨跌幅低2高
    public static final String OPTIONAL_DERIVATIVES_RATE_LOW2HIGH = "OPTIONAL_DERIVATIVES_RATE_LOW2HIGH";
    //自选 名称A到Z
    public static final String OPTIONAL_DERIVATIVES_NAME_A2Z = "OPTIONAL_DERIVATIVES_NAME_A2Z";
    //自选 名称Z到A
    public static final String OPTIONAL_DERIVATIVES_NAME_Z2A = "OPTIONAL_DERIVATIVES_NAME_Z2A";



    //现货列表
    public static final String SPOT_ALL = "SPOT_ALL";
    //现货 价格高2低
    public static final String SPOT_PRICE_HIGH2LOW = "SPOT_PRICE_HIGH2LOW";
    //现货 价格低2高
    public static final String SPOT_PRICE_LOW2HIGH = "SPOT_PRICE_LOW2HIGH";
    //现货 涨跌幅高2低
    public static final String SPOT_RATE_HIGH2LOW = "SPOT_RATE_HIGH2LOW";
    //现货 涨跌幅低2高
    public static final String SPOT_RATE_LOW2HIGH = "SPOT_RATE_LOW2HIGH";
    //现货 名称A到Z
    public static final String SPOT_NAME_A2Z = "SPOT_NAME_A2Z";
    //现货 名称Z到A
    public static final String SPOT_NAME_Z2A = "SPOT_NAME_Z2A";

    //现货defi列表
    public static final String SPOT_DEFI_ALL = "SPOT_DEFI_ALL";
    //现货 价格高2低
    public static final String SPOT_DEFI_PRICE_HIGH2LOW = "SPOT_DEFI_PRICE_HIGH2LOW";
    //现货 价格低2高
    public static final String SPOT_DEFI_PRICE_LOW2HIGH = "SPOT_DEFI_PRICE_LOW2HIGH";
    //现货 涨跌幅高2低
    public static final String SPOT_DEFI_RATE_HIGH2LOW = "SPOT_DEFI_RATE_HIGH2LOW";
    //现货 涨跌幅低2高
    public static final String SPOT_DEFI_RATE_LOW2HIGH = "SPOT_DEFI_RATE_LOW2HIGH";
    //现货 名称A到Z
    public static final String SPOT_DEFI_NAME_A2Z = "SPOT_DEFI_NAME_A2Z";
    //现货 名称Z到A
    public static final String SPOT_DEFI_NAME_Z2A = "SPOT_DEFI_NAME_Z2A";

    //现货pos列表
    public static final String SPOT_POS_ALL = "SPOT_POS_ALL";
    //现货 价格高2低
    public static final String SPOT_POS_PRICE_HIGH2LOW = "SPOT_POS_PRICE_HIGH2LOW";
    //现货 价格低2高
    public static final String SPOT_POS_PRICE_LOW2HIGH = "SPOT_POS_PRICE_LOW2HIGH";
    //现货 涨跌幅高2低
    public static final String SPOT_POS_RATE_HIGH2LOW = "SPOT_POS_RATE_HIGH2LOW";
    //现货 涨跌幅低2高
    public static final String SPOT_POS_RATE_LOW2HIGH = "SPOT_POS_RATE_LOW2HIGH";
    //现货 名称A到Z
    public static final String SPOT_POS_NAME_A2Z = "SPOT_POS_NAME_A2Z";
    //现货 名称Z到A
    public static final String SPOT_POS_NAME_Z2A = "SPOT_POS_NAME_Z2A";

    //合约列表
    public static final String CONTRACT_ALL = "CONTRACT_ALL";
    //合约 价格高2低
    public static final String CONTRACT_PRICE_HIGH2LOW = "CONTRACT_PRICE_HIGH2LOW";
    //合约 价格低2高
    public static final String CONTRACT_PRICE_LOW2HIGH = "CONTRACT_PRICE_LOW2HIGH";
    //合约 涨跌幅高2低
    public static final String CONTRACT_RATE_HIGH2LOW = "CONTRACT_RATE_HIGH2LOW";
    //合约 涨跌幅低2高
    public static final String CONTRACT_RATE_LOW2HIGH = "CONTRACT_RATE_LOW2HIGH";
    //合约 名称A到Z
    public static final String CONTRACT_NAME_A2Z = "CONTRACT_NAME_A2Z";
    //合约 名称Z到A
    public static final String CONTRACT_NAME_Z2A = "CONTRACT_NAME_Z2A";


    //热门
    public static final String HOME_HOT = "HOME_HOT";
    public static final String HOME_LIST= "HOME_LIST";



    //衍生品列表
    public static final String DERIVATIVES_ALL = "DERIVATIVES_ALL";
    //衍生品 价格高2低
    public static final String DERIVATIVES_PRICE_HIGH2LOW = "DERIVATIVES_PRICE_HIGH2LOW";
    //衍生品 价格低2高
    public static final String DERIVATIVES_PRICE_LOW2HIGH = "DERIVATIVES_PRICE_LOW2HIGH";
    //衍生品 涨跌幅高2低
    public static final String DERIVATIVES_RATE_HIGH2LOW = "DERIVATIVES_RATE_HIGH2LOW";
    //衍生品 涨跌幅低2高
    public static final String DERIVATIVES_RATE_LOW2HIGH = "DERIVATIVES_RATE_LOW2HIGH";
    //衍生品 名称A到Z
    public static final String DERIVATIVES_NAME_A2Z = "DERIVATIVES_NAME_A2Z";
    //衍生品 名称Z到A
    public static final String DERIVATIVES_NAME_Z2A = "DERIVATIVES_NAME_Z2A";



    //外汇列表
    public static final String FOREIGN_EXCHANGE_ALL = "FOREIGN_EXCHANGE_ALL";
    //衍生品 价格高2低
    public static final String FOREIGN_EXCHANGE_PRICE_HIGH2LOW = "FOREIGN_EXCHANGE_PRICE_HIGH2LOW";
    //衍生品 价格低2高
    public static final String FOREIGN_EXCHANGE_PRICE_LOW2HIGH = "FOREIGN_EXCHANGE_PRICE_LOW2HIGH";
    //衍生品 涨跌幅高2低
    public static final String FOREIGN_EXCHANGE_RATE_HIGH2LOW = "FOREIGN_EXCHANGE_RATE_HIGH2LOW";
    //衍生品 涨跌幅低2高
    public static final String FOREIGN_EXCHANGE_RATE_LOW2HIGH = "FOREIGN_EXCHANGE_RATE_LOW2HIGH";
    //衍生品 名称A到Z
    public static final String FOREIGN_EXCHANGE_NAME_A2Z = "FOREIGN_EXCHANGE_NAME_A2Z";
    //衍生品 名称Z到A
    public static final String FOREIGN_EXCHANGE_NAME_Z2A = "FOREIGN_EXCHANGE_NAME_Z2A";

    /*商品合约*/
    public static final String KEY_COMMODITY = "KEY_COMMODITY";
    /*账户头像保存名称*/
    public static final String FIRST_OPEN = "first_open"; //安全认证信息
    /*缓存国家代码*/
    public static final String COUNTRY_CODE = "country"; //安全认证信息

    /*用户登录*/
    public static final String LOGIN = "login";
    /*佣金比例*/
    public static final String KEY_UNION = "KEY_UNION";
    /*用户区号*/
    public static final String USER_COUNTRY_CODE = "user_country_code";
    /*用户国家*/
    public static final String USER_COUNTRY_NAME = "user_country_name";
    /*手机号*/
    public static final String USER_MOBILE = "user_mobile";
    /*邮箱号*/
    public static final String USER_EMAIL = "user_email";
    /*行情地址*/
    public static final String QUOTE_HOST = "QUOTE_HOST";
    /*行情合约号*/
    public static final String QUOTE_CODE = "QUOTE_CODE";
    /*合约号详情*/
    public static final String QUOTE_DETAIL = "QUOTE_DETAIL";
    /*主题切换*/
    public static final String KEY_THEME = "KEY_THEME";
    /*多语言*/
    public static final String KEY_LANGUAGE = "KEY_LANGUAGE";
    /*是否持仓过夜*/
    public static final String KEY_DEFER = "KEY_DEFER";
    /*是否下单确认*/
    public static final String KEY_OPEN_SURE = "KEY_OPEN";
    /*是否平仓确认*/
    public static final String KEY_CLOSE_SURE = "KEY_CLOSE";
    /*止盈比例的默认值下标*/
    public static final String INDEX_PROFIT = "INDEX_PROFIT";
    /*止盈比例的默认值*/
    public static final String VALUE_PROFIT = "VALUE_PROFIT";
    /*止损比例的默认值下标*/
    public static final String INDEX_LOSS = "INDEX_LOSS";
    /*止损比例的默认值下标*/
    public static final String VALUE_LOSS = "VALUE_LOSS";
    /*汇率*/
    public static final String USD_RATE = "USD_RATE";
    /*可支持的currency*/
    public static final String SUPPORT_CURRENCY = "SUPPORT_CURRENCY";
    /*抵扣金额*/
    public static final String PRIZE_TRADE = "prizeTrade";
    /*本地自选*/
    public static final String KEY_OPTIONAL = "KEY_OPTIONAL";
    /*是否开启极验*/
    public static final String KEY_VERIFICATION = "KEY_VERIFICATION";
    /*汇率*/
    public static final String USDT = "USDT";
    public static final String BTC = "BTC";
    public static final String ETH = "ETH";
    public static final String XRP = "XRP";
    public static final String HT = "HT";
    public static final String TRX = "TRX";
    public static final String LINK = "LINK";
    //货币
    public static final String CURRENCY = "CURRENCY";
    public static final String CURRENCY_LIST = "CURRENCY_LIST";
    public static final String CURRENCY_INDEX = "CURRENCY_INDEX";

    //LIST
    public static final String RATE_LIST = "RATE_LIST";
    //用户修改手机
    public static final String CHANGE_MOBILE = "CHANGE_MOBILE";
    public static final String CHANGE_EMAIL = "CHANGE_EMAIL";
    //合约号
    public static final String CONTRACT_ID = "CONTRACT_ID";
    //个人详情
    public static final String DETAIL = "DETAIL";
    //注册成功弹窗
    public static final String POP_LOGIN = "POP_LOGIN";
    //小数位
    public static final String SCALE = "SCALE";


    public static final String ZH_SIMPLE = "zh_CN";
    public static final String VI_VN = "vi_VN";
    public static final String IN_ID = "in_ID";
    public static final String ZH_TRADITIONAL = "zh_traditional";
    public static final String JA_JP = "ja_JP";
    public static final String EN_US = "en_US";
    public static final String KO_KR = "ko_KR";
    public static final String RU_RU = "ru_RU";
    public static final String PT_PT = "pt_PT";


    public static final String type_style = "style";
    public static final String type_rate = "rate";
    public static final String type_draw = "draw";
    public static final String type_day = "day";

    public static final int CODE_FILTER = 100;

    public static final String KEY_FILTER_RESULT= "FILTER_RESULT";
}
