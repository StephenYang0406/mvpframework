package com.stephen.demo.model

import com.stephen.mvpframework.model.BaseVo

/**
 * Created by Stephen on 2018/12/4.
 * StephenYoung0406@hotmail.com
 * <(￣ c￣)y▂ξ
 */
class CustomerVo : BaseVo() {
    //客户ID
    var customerId: Int? = null
    //顾客是哪个承包商的，为空则是美通直接客户'
    var contractorId: Int? = null
    //客户类型，1=网配（普通）客户（每次结算）  2=外配（固定）客户（账期计算）
    var customerType: String? = null
    //客户属性，P=个人（Person）  O=企业或单位（Org）S=商超,小卖部（Shop）， java中用枚举表示，不再建表维护',
    var customerAttr: String? = null
    //'客户加入的客户组id，客户只能加入一个客户组'
    var customerGroupId: Int? = null
    //客户登录账号
    var username: String? = null
    //    //客户密码
    //    public String password;
    //个人为真实姓名，企业或单位则为单位名称
    var realname: String? = null
    //'客户联系电话或手机号'
    var mobile: String? = null
    //'账号建立时间
    var createTime: String? = null
    //用户状态，0=禁用,1=可用
    var enabled: Int? = null
    //客户的积分
    var point: Int? = null
}