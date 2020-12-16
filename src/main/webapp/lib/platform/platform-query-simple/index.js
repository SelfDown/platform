var _config =   {
          name:"platform_query_simple",
          fields:[
              {
              showName:"",
              name:"service_type",
              must_send:false,
              must_save:true,
              type:"hidden",
              placeholder:"",
              value:"service_platform_query_simple"
              },
              {
                showName:"",
                name:"type",
                must_send:false,
                must_save:true,
                type:"hidden",
                placeholder:"",
                value:"platform_query_simple"
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
                showName:"table,数据库表",
                name:"table",
                must_send:false,
                must_save:true,
                type:"input",
                placeholder:"查询数据库表",
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
                showName:'搜索字段，between_and为字符串,第四列为json {"operation":"=","origin_field":"name","join_sql":"left join user on table_name.pk_user = user.id"}',
                name:"search_fields",
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
                      "field_4":'{"operation":"="}',
                      "field_4_placeholder":"操作 in、=、like、between_and、date_between_and",
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
                      "field_4":'{"operation":"="}',
                      "field_4_placeholder":"操作:in、=、like、between_and、date_between_and",
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
                      "field_4_placeholder":"操作:in、=、like、between_and、date_between_and",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    }
                ]
              },

              {
                showName:'默认搜索字段，between_and为字符串,第四列为json {"operation":"=","origin_field":"name","join_sql":"left join user on table_name.pk_user = user.id"}',
                name:"default_search_fields",
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
                      "field_4":'{"operation":"="}',
                      "field_4_placeholder":"操作 in、=、like、between_and、date_between_and",
                      "field_5":"0",
                      "field_5_placeholder":"默认值"
                    },

                    
                    ]
              },

              {
                showName:"显示字段",
                name:"show_fields",
                must_send:false,
                must_save:false,
                type:"array",
                placeholder:'显示字段,第三列为json {"can_show":"true","can_update":"true"},第四列为json {"origin_field":"name","join_sql":"left join user on table_name.pk_user = user.id"}',
                value:[{"field_1":"",
                    "field_1_placeholder":"字段",
                    "field_2":"",
                    "field_2_placeholder":"描述",
                    "field_3":'{"can_show":"true","can_update":"true"}',
                    "field_3_placeholder":"显示:true/false,更新:true/false",
                    "field_4":"{}",
                    "field_4_placeholder":"关联表",
                    "field_5":"",
                    "field_5_placeholder":"备用",
                    }
                    ]
              },


              {
                showName:"count_id ,统计字段",
                name:"count_id",
                must_send:false,
                must_save:true,
                type:"input",
                placeholder:"id",
                value:"id"
              },


              {
                showName:"排序字段",
                name:"order_fields",
                must_send:false,
                must_save:false,
                type:"array",
                placeholder:"显示字段",
                value:[{"field_1":"",
                    "field_1_placeholder":"字段",
                    "field_2":"",
                    "field_2_placeholder":"描述",
                    "field_3":"",
                    "field_3_placeholder":"",
                    "field_4":"",
                    "field_4_placeholder":"",
                    "field_5":"",
                    "field_5_placeholder":"desc|asc",
                    }
                    ]
              }


              
              ]

        }

function get_config(){
    return _config
}