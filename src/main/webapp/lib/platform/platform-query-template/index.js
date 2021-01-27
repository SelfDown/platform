var _config =   {
          name:"platform_query_template",
          fields:[
              {
              showName:"",
              name:"service_type",
              must_send:false,
              must_save:true,
              type:"hidden",
              placeholder:"",
              value:"service_platform_query_template"
              },
              {
                showName:"",
                name:"type",
                must_send:false,
                must_save:true,
                type:"hidden",
                placeholder:"",
                value:"platform_query_template"
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
                showName:"action_type,请求类型，http,service",
                name:"action_type",
                must_send:false,
                must_save:true,
                type:"input",
                placeholder:"",
                value:"http"
              },

               {
                showName:"preview,是否预览sql",
                name:"preview",
                must_send:false,
                must_save:false,
                type:"input",
                placeholder:"",
                value:"false"
              },



              {
                showName:"toTree,是否转成树形结构，以逗号隔开，如'id,parent_id,children'字段，空就是列表,虚拟节点delete_flag=0,id=0 ",
                name:"toTree",
                must_send:false,
                must_save:false,
                type:"input",
                placeholder:"是否转成树形结构，空就是列表",
                value:""
              },

              {
                showName:'搜索字段',
                name:"fields",
                must_send:false,
                must_save:false,
                type:"array",
                placeholder:"搜索字段",
                value:[

                    {
                      "field_1":"page",
                      "field_1_placeholder":"字段名称",
                      "field_2":"页数",
                      "field_2_placeholder":"描述",
                      "field_3":"I",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                      "field_4":'',
                      "field_4_placeholder":"",
                      "field_5":"1",
                      "field_5_placeholder":"默认值"
                    },

                    {
                      "field_1":"size",
                      "field_1_placeholder":"字段名称",
                      "field_2":"数量",
                      "field_2_placeholder":"描述",
                      "field_3":"I",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                      "field_4":'',
                      "field_4_placeholder":"",
                      "field_5":"10",
                      "field_5_placeholder":"默认值"
                    },

                    {"field_1":"",
                      "field_1_placeholder":"字段名称",
                      "field_2":"",
                      "field_2_placeholder":"描述",
                      "field_3":"",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列校验规则。如S，must。表示类型为字符串。必须传",
                      "field_4":"",
                      "field_4_placeholder":"",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    }
                ]
              },

              {
                showName:'默认搜索字段',
                name:"default_fields",
                must_send:false,
                must_save:false,
                type:"array",
                placeholder:"搜索字段",
                value:[

                    {
                      "field_1":"delete_flag",
                      "field_1_placeholder":"字段名称",
                      "field_2":"是否删除",
                      "field_2_placeholder":"描述",
                      "field_3":"S",
                      "field_3_placeholder":"类型 S、I",
                      "field_4":'',
                      "field_4_placeholder":"",
                      "field_5":"0",
                      "field_5_placeholder":"默认值"
                    },


                    ]
              },
                {
                  showName:"data_sql,显示数据SQL",
                  name:"data_sql",
                  must_send:false,
                  must_save:true,
                  type:"textarea",
                  placeholder:"",
                  value:"select * from shoes"
                },

                {
                  showName:"count_sql,统计数据SQL",
                  name:"count_sql",
                  must_send:false,
                  must_save:true,
                  type:"textarea",
                  placeholder:"",
                  value:"select count(id) as count from shoes"
                },

              {
                showName:"show_fields,显示字段",
                name:"show_fields",
                must_send:false,
                must_save:false,
                type:"array",
                placeholder:'显示字段',
                value:[{"field_1":"name",
                    "field_1_placeholder":"字段",
                    "field_2":"名称",
                    "field_2_placeholder":"描述",
                    "field_3":'',
                    "field_3_placeholder":"",
                    "field_4":"{}",
                    "field_4_placeholder":"",
                    "field_5":"",
                    "field_5_placeholder":"备用",
                    }
                    ]
              },


              {
                showName:"count ,返回统计字段",
                name:"count",
                must_send:false,
                must_save:true,
                type:"input",
                placeholder:"count",
                value:"count"
              },





              ]

        }

function get_config(){
    return _config
}