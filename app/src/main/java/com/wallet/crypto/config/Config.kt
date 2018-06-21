package cn.magicwindow.core.config

/**
 * @author Aaron
 * @email aaron@magicwindow.cn
 * @date 18/03/2018 22:33
 * @description
 */
object Config {

    const val MW_CHANNEL = "MW_CHANNEL"

    //对话框请求码
    const val DIALOG_OK_REQUEST_CODE = 100
    const val DIALOG_CANCEL_REQUEST_CODE = 1000
    const val DIALOG_SMS_REQUEST_CODE = 1001
    const val DIALOG_CREATE_PWD_REQUEST_CODE = 1002
    const val DIALOG_CAN_NOT_PAY_REQUEST_CODE = 1003
    const val DIALOG_PAY_ERR_REQUEST_CODE = 1004
    const val DIALOG_PAY_FAIL_REQUEST_CODE = 1005
    const val DIALOG_ACCOUNT_FROZEN_REQUEST_CODE = 1006
    const val DIALOG_PAY_WITH_PWD_REQUEST_CODE_OTHER = 1007
    const val DIALOG_PAY_WITH_PWD_COME_WITH_MARKET = 1008
    const val DIALOG_PAY_WITH_PWD_COME_WITH_RED_POCKET = 1009
    const val DIALOG_PAY_FAIL_RESET_REQUEST_CODE = 1010// chulongxu新增payfail 重置密码按钮
    const val DIALOG_SHARE_FAIL_GO_TO_SEE = 2000
    const val DIALOG_SHARE_FAIL_SHARE = 2001
    const val DIALOG_WITHDRAW_REQUEST_CODE = 1011//shuqing 提现按钮点击时的确认弹窗
    const val DIALOG_SELECT_WX_GROUP = 2002

    const val COUNT_DOWN_TIMER = 60000L

    const val STATUS_BAR_ALPHA = 30

    // carrier相关信息
    const val CHINA_MOBILE = "1"
    const val CHINA_UNICOM = "2"
    const val CHINA_TELECOM = "3"
    const val CHINA_TIETONG = "4"

    const val NETWORK_WIFI = "0"
    const val NETWORK_2G = "1"
    const val NETWORK_3G = "2"
    const val NETWORK_4G = "3"
    const val NO_NETWORK = "-1"

    const val NETWORK_OK = 200
    const val NETWORK_RESPONSE_OK = 0
    const val ACTIVITY_KOL_REQUEST_CODE_REFRESH = 1001

    const val NETWORK_RESPONSE_LOGIN_INVALIDATE = 997 //登录失效
    const val NETWORK_RESPONSE_INVALIDATE = 998 //checkToken等接口 token失效
    const val NETWORK_RESPONSE_TOKEN_EXPIRED = 999 //checkToken等接口 token过期
    const val NETWORK_RESPONSE_PAYMENT_PASSWORD_ERROR = 991   //支付密码错误
    const val NETWORK_RESPONSE_PAYMENT_ACCOUNT_FROZEN = 992   //支付账户已被冻结，将在24小时后恢复
    const val NETWORK_RESPONSE_IP_CHANGED = 993   //IP或者设备变更
    const val NETWORK_RESPONSE_UAT_NOT_ENOUGH = 980   //UAT余额不足
    const val NETWORK_RESPONSE_CODE_TOO_OFEN = 4   //短信发送太频繁
    const val NETWORK_RESPONSE_CODE_VALIDATE_ERROR = 5   //验证码错误
    const val NETWORKCODE_VALIDAT_TOOMANY = 6   //验证码错误次数过多
    //业务逻辑错误
    const val NETWORK_RESPONSE_2 = 2

    // Router URI

    // BUNDLE KEY

}