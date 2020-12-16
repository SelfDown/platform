
var _config = {
    name:"server_ssh",
    fields:[
        {
        showName:"",
        name:"service_type",
        must_send:false,
        must_save:true,
        type:"hidden",
        placeholder:"",
        value:"service_server_ssh"
        },
        {
            showName:"",
            name:"type",
            must_send:false,
            must_save:true,
            type:"hidden",
            placeholder:"",
            value:"server_ssh"
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
            showName:"fields,搜索字段",
            name:"fields",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"搜索字段",
            value:[

                  {
                    "field_1":"host",
                    "field_1_placeholder":"服务器IP",
                    "field_2":"服务器IP",
                    "field_2_placeholder":"描述",
                    "field_3":"S",
                    "field_3_placeholder":"",
                    "field_4":"=",
                    "field_4_placeholder":"",
                    "field_5":"192.168.3.131",
                    "field_5_placeholder":"默认值"
                },

                {
                    "field_1":"username",
                    "field_1_placeholder":"用户名",
                    "field_2":"用户名",
                    "field_2_placeholder":"描述",
                    "field_3":"S",
                    "field_3_placeholder":"",
                    "field_4":"=",
                    "field_4_placeholder":"",
                    "field_5":"root",
                    "field_5_placeholder":"默认值"
                },

                {"field_1":"password",
                    "field_1_placeholder":"密码",
                    "field_2":"",
                    "field_2_placeholder":"描述",
                    "field_3":"S",
                    "field_3_placeholder":"S",
                    "field_4":"",
                    "field_4_placeholder":"=",
                    "field_5":"zhang@888",
                    "field_5_placeholder":"默认值"
                },
                  {"field_1":"port",
                    "field_1_placeholder":"端口",
                    "field_2":"端口",
                    "field_2_placeholder":"描述",
                    "field_3":"I",
                    "field_3_placeholder":"I",
                    "field_4":"",
                    "field_4_placeholder":"=",
                    "field_5":"22",
                    "field_5_placeholder":"默认值"
                },

                  {"field_1":"timeout",
                    "field_1_placeholder":"超时毫秒",
                    "field_2":"超时毫秒",
                    "field_2_placeholder":"描述",
                    "field_3":"I",
                    "field_3_placeholder":"I",
                    "field_4":"",
                    "field_4_placeholder":"=",
                    "field_5":"6000",
                    "field_5_placeholder":"默认值"
                }
            ]
    },


      {
            showName:"default_fields,默认字段",
            name:"default_fields",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"搜索字段",
            value:[
                    {
                        "field_1":"",
                        "field_1_placeholder":"",
                        "field_2":"",
                        "field_2_placeholder":"描述",
                        "field_3":"S",
                        "field_3_placeholder":"",
                        "field_4":"=",
                        "field_4_placeholder":"",
                        "field_5":"",
                        "field_5_placeholder":"默认值"
                    },


            ]
        },
        {
            showName:"prepare,执行前检查，检查登录，返回session",
            name:"prepare",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"搜索字段",
            value:[
                    {
                        "field_1":"ssh_login",
                        "field_1_placeholder":"",
                        "field_2":"",
                        "field_2_placeholder":"描述",
                        "field_3":'{"service": "service_ssh_login","fields": {"host": "${host}","username": "${username}","password": "${password}"}}',
                        "field_3_placeholder":"",
                        "field_4":"=",
                        "field_4_placeholder":"",
                        "field_5":'{"success": true}',
                        "field_5_placeholder":"默认值"
                    },


            ]
        },
        {
            showName:"execute,执行",
            name:"execute",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"搜索字段",
            value:[
                    {
                        "field_1":"check_system",
                        "field_1_placeholder":"",
                        "field_2":"查看系统版本",
                        "field_2_placeholder":"描述",
                        "field_3":'{"result2web":true,"expect":"Linux"}',
                        "field_3_placeholder":"",
                        "field_4":"{}",
                        "field_4_placeholder":"",
                        "field_5":'uname -a',
                        "field_5_placeholder":"默认值"
                    }, {
                        "field_1":"check_memory",
                        "field_1_placeholder":"",
                        "field_2":"查看系统版本",
                        "field_2_placeholder":"描述",
                        "field_3":'{"result2web":true,"expect":"MemTotal"}',
                        "field_3_placeholder":"",
                        "field_4":"{}",
                        "field_4_placeholder":"",
                        "field_5":'cat /proc/meminfo | grep MemTotal',
                        "field_5_placeholder":"默认值"
                    },


            ]
        }





    ]
    }

function get_config(){
    return _config
}