
var _config = {
    name:"server_scan",
    fields:[
        {
        showName:"",
        name:"service_type",
        must_send:false,
        must_save:true,
        type:"hidden",
        placeholder:"",
        value:"service_server_scan"
        },
        {
            showName:"",
            name:"type",
            must_send:false,
            must_save:true,
            type:"hidden",
            placeholder:"",
            value:"server_scan"
        },
        {
            showName:"service",
            name:"service",
            must_send:true,
            must_save:true,
            type:"input",
            placeholder:"服务访问名称",
        },
        {
            showName:"备注",
            name:"backup",
            must_send:false,
            must_save:false,
            type:"input",
            placeholder:"",
        },
        {
            showName:"login_require,是否需要登陆",
            name:"login_require",
            must_send:false,
            must_save:true,
            type:"input",
            placeholder:"是否需要登陆",
            value:"true"
        },

        {
            showName:"fields,搜索字段，between_and为字符串",
            name:"fields",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"搜索字段",
            value:[

                    {
                        "field_1":"ip_matcher",
                        "field_1_placeholder":"字段名称",
                        "field_2":"IP段或者多个ip逗号分隔，如：192.169.2.10/30 or 192.169.2.10-30 or 192.169.2.10,192.168.2.11 ",
                        "field_2_placeholder":"描述",
                        "field_3":"S",
                        "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                        "field_4":"=",
                        "field_4_placeholder":"操作 in、=、like、between_and、date_between_and",
                        "field_5":"192.168.20.163",
                        "field_5_placeholder":"默认值"
                    },

                    {
                        "field_1":"port",
                        "field_1_placeholder":"字段名称",
                        "field_2":"端口",
                        "field_2_placeholder":"描述",
                        "field_3":"S",
                        "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                        "field_4":"=",
                        "field_4_placeholder":"操作:in、=、like、between_and、date_between_and",
                        "field_5":"22",
                        "field_5_placeholder":"默认值"
                    },
                    {"field_1":"thread_number",
                        "field_1_placeholder":"字段名称",
                        "field_2":"线程数",
                        "field_2_placeholder":"描述",
                        "field_3":"I",
                        "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                        "field_4":"=",
                        "field_4_placeholder":"操作:in、=、like、between_and、date_between_and",
                        "field_5":"5",
                        "field_5_placeholder":"默认值"
                    },
                    {"field_1":"show_connect",
                        "field_1_placeholder":"字段名称",
                        "field_2":"只显示能连接",
                        "field_2_placeholder":"描述",
                        "field_3":"S",
                        "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                        "field_4":"",
                        "field_4_placeholder":"",
                        "field_5":"",
                        "field_5_placeholder":"默认值"
                    },


            ]
    },
        {
            showName:"default_fields,默认配置",
            name:"default_fields",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"搜索字段",
            value:[
                    {
                        "field_1":"timeout",
                        "field_1_placeholder":"字段名称",
                        "field_2":"连接超时",
                        "field_2_placeholder":"描述",
                        "field_3":"S",
                        "field_3_placeholder":"类型 S、I",
                        "field_4":"",
                        "field_4_placeholder":"",
                        "field_5":"200",
                        "field_5_placeholder":"默认值"
                    },
                      {
                          "field_1":"regex",
                          "field_1_placeholder":"字段名称",
                          "field_2":"判断IP正则",
                          "field_2_placeholder":"描述",
                          "field_3":"S",
                          "field_3_placeholder":"类型 S、I",
                          "field_4":"",
                          "field_4_placeholder":"",
                          "field_5":"",
                          "field_5_placeholder":"默认值"
                        },

                         {
                            "field_1":"wait",
                            "field_1_placeholder":"字段名称",
                            "field_2":"线程等待时间，ms",
                            "field_2_placeholder":"描述",
                            "field_3":"S",
                            "field_3_placeholder":"类型 S、I",
                            "field_4":"",
                            "field_4_placeholder":"",
                            "field_5":"50",
                            "field_5_placeholder":"默认值"
                          },






                    ]
        },

           {
                    showName:"return_type,返回类型,list,dict",
                    name:"return_type",
                    must_send:false,
                    must_save:true,
                    type:"input",
                    placeholder:"返回类型",
                    value:"list"
                },



        {
            showName:"显示字段",
            name:"show_fields",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"显示字段",
            value:[{"field_1":"ip",
                    "field_1_placeholder":"ip",
                    "field_2":"",
                    "field_2_placeholder":"描述",
                    "field_3":"S",
                    "field_3_placeholder":"",
                    "field_4":"",
                    "field_4_placeholder":"",
                    "field_5":"",
                    "field_5_placeholder":"备用",
                    },
                    {"field_1":"port",
                    "field_1_placeholder":"端口",
                    "field_2":"例如22,或者1-1024",
                    "field_2_placeholder":"端口",
                    "field_3":"",
                    "field_3_placeholder":"S",
                    "field_4":"",
                    "field_4_placeholder":"",
                    "field_5":"",
                    "field_5_placeholder":"备用",
                    },
                     {
                        "field_1":"spend",
                        "field_1_placeholder":"",
                        "field_2":"耗时,毫秒",
                        "field_2_placeholder":"描述",
                        "field_3":"S",
                        "field_3_placeholder":"",
                        "field_4":"",
                        "field_4_placeholder":"",
                        "field_5":"",
                        "field_5_placeholder":"备用",
                      }
             ]
        },
    ]
    }

function get_config(){
    return _config
}