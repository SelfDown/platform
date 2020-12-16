var _config ={
          name:"version_iteration",
          fields:[
              {
              showName:"",
              name:"service_type",
              must_send:false,
              must_save:true,
              type:"hidden",
              placeholder:"",
              value:"service_version_iteration"
              },
              {
                showName:"",
                name:"type",
                must_send:false,
                must_save:true,
                type:"hidden",
                placeholder:"",
                value:"version_iteration"
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
                value:"true"
              },
            
              {
                showName:'修改数据字段',
                name:"fields",
                must_send:false,
                must_save:false,
                type:"array",
                placeholder:"登陆字段",
                value:[

                    {
                      "field_1":"name",
                      "field_1_placeholder":"字段名称",
                      "field_2":"名称",
                      "field_2_placeholder":"描述",
                      "field_3":"S,must",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                      "field_4":"",
                      "field_4_placeholder":"是否伪删除,必须为JSON",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    },
                    {
                      "field_1":"url",
                      "field_1_placeholder":"字段名称",
                      "field_2":"访问地址",
                      "field_2_placeholder":"描述",
                      "field_3":"S,must",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                      "field_4":'',
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
                    
                    {"field_1":"id",
                    "field_1_placeholder":"字段",
                    "field_2":"id",
                    "field_2_placeholder":"描述",
                    "field_3":"S",
                    "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                    "field_4":"",
                    "field_4_placeholder":"",
                    "field_5":'',
                    "field_5_placeholder":"默认值",
                    }
                  ]
              }
              ]
        }

function get_config(){
    return _config
}