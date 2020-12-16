var _config =   {
          name:"platform_login",
          fields:[
              {
              showName:"",
              name:"service_type",
              must_send:false,
              must_save:true,
              type:"hidden",
              placeholder:"",
              value:"service_platform_login"
              },
              {
                showName:"",
                name:"type",
                must_send:false,
                must_save:true,
                type:"hidden",
                placeholder:"",
                value:"platform_login"
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
                showName:"是否需要登陆，login_require",
                name:"login_require",
                must_send:false,
                must_save:true,
                type:"input",
                placeholder:"是否需要登陆",
                value:"false"
              },
              {
                showName:"是否记住登陆，remember_me",
                name:"remember_me",
                must_send:false,
                must_save:true,
                type:"input",
                placeholder:"是否记住登陆",
                value:"false"
              },
              {
                showName:'登陆字段',
                name:"fields",
                must_send:false,
                must_save:false,
                type:"array",
                placeholder:"登陆字段",
                value:[

                    {
                      "field_1":"username",
                      "field_1_placeholder":"字段名称",
                      "field_2":"用户名",
                      "field_2_placeholder":"描述",
                      "field_3":"S,must",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                      "field_4":"",
                      "field_4_placeholder":"是否伪删除,必须为JSON",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    },
                    {
                      "field_1":"password",
                      "field_1_placeholder":"字段名称",
                      "field_2":"密码",
                      "field_2_placeholder":"描述",
                      "field_3":"S,must",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                      "field_4":'',
                      "field_4_placeholder":"是否伪删除,必须为JSON",
                      'field_5':'',
                      "field_5_placeholder":"默认值"
                    },
                    {
                      "field_1":"alwaysLogin",
                      "field_1_placeholder":"字段名称",
                      "field_2":"是否强制登陆",
                      "field_2_placeholder":"描述",
                      "field_3":"S",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                      "field_4":'',
                      "field_4_placeholder":"是否伪删除,必须为JSON",
                      'field_5':'false',
                      "field_5_placeholder":"默认值"
                    },


                    
                    ]
              }
              ]
        }

function get_config(){
    return _config
}