var _config =  {
      name:"platform_update",
      fields:[
          {
          showName:"",
          name:"service_type",
          must_send:false,
          must_save:true,
          type:"hidden",
          placeholder:"",
          value:"service_platform_update"
          },
          {
            showName:"",
            name:"type",
            must_send:false,
            must_save:true,
            type:"hidden",
            placeholder:"",
            value:"platform_update"
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
            showName:"action_type,请求类型，http,service",
            name:"action_type",
            must_send:false,
            must_save:true,
            type:"input",
            placeholder:"",
            value:"http"
          },
          {
            showName:"数据库表",
            name:"table_name",
            must_send:false,
            must_save:true,
            type:"input",
            placeholder:"数据库表",
          },
          {
            showName:'修改数据字段,使用\"，默认字段同时存在，优先数据字段,{"delete_field_mode":{"username":"$now$","deleteflag":0},"delete_service_mode":{"service":"user_exitst","search_fields":{"username":"$now$"}}}',
            name:"fields",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"修改数据字段",
            value:[

                {
                  "field_1":"nickname",
                  "field_1_placeholder":"字段名称",
                  "field_2":"名称",
                  "field_2_placeholder":"描述",
                  "field_3":"S",
                  "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                  "field_4":"",
                  "field_4_placeholder":"是否伪删除,必须为JSON",
                  "field_5":"",
                  "field_5_placeholder":"默认值"
                },
                {
                  "field_1":"username",
                  "field_1_placeholder":"字段名称",
                  "field_2":"用户名",
                  "field_2_placeholder":"描述",
                  "field_3":"S,must",
                  "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                  "field_4":'{"delete_field_mode":{"username":"$now$","deleteflag":0}}',
                  "field_4_placeholder":"是否伪删除,必须为JSON",
                  'field_5':'',
                  "field_5_placeholder":"默认值"
                },
                ]
          },

          

          {
            showName:"条件筛选字段",
            name:"filters",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"条件筛选字段",
            value:[
                
                {"field_1":"pk_user",
                "field_1_placeholder":"字段",
                "field_2":"是否删除",
                "field_2_placeholder":"描述",
                "field_3":"S",
                "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                "field_4":"",
                "field_4_placeholder":"",
                "field_5":'',
                "field_5_placeholder":"默认值",
                }
              ]
          },
          {
            showName:"默认条件字段",
            name:"default_filters",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"默认条件字段",
            value:[
                
                
                {"field_1":"deleteflag",
                "field_1_placeholder":"字段",
                "field_2":"是否删除",
                "field_2_placeholder":"描述",
                "field_3":"I",
                "field_3_placeholder":"类型 S、I、D、USER_ID、DATETIME,UUID",
                "field_4":"",
                "field_4_placeholder":"",
                "field_5":"0",
                "field_5_placeholder":"默认值",
                }
              ]
          },
          {
            showName:"默认字段",
            name:"default_fields",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"默认字段",
            value:[
                
                {"field_1":"updateby",
                "field_1_placeholder":"字段",
                "field_2":"是否删除",
                "field_2_placeholder":"描述",
                "field_3":"USER_ID",
                "field_3_placeholder":"类型 S、I、D、USER_ID、DATETIME,UUID",
                "field_4":"",
                "field_4_placeholder":"",
                "field_5":"",
                "field_5_placeholder":"默认值",
                },
                {"field_1":"updatetime",
                "field_1_placeholder":"字段",
                "field_2":"是否删除",
                "field_2_placeholder":"描述",
                "field_3":"DATETIME",
                "field_3_placeholder":"类型 S、I、D、USER_ID、DATETIME,UUID",
                "field_4":"",
                "field_4_placeholder":"",
                "field_5":"",
                "field_5_placeholder":"默认值",
                }
              ]
          },
          
          ]
    }

function get_config(){
    return _config
}