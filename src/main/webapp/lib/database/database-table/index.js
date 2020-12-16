var _config ={
          name:"database_table",
          fields:[
              {
              showName:"",
              name:"service_type",
              must_send:false,
              must_save:true,
              type:"hidden",
              placeholder:"",
              value:"service_database_table"
              },
              {
                showName:"",
                name:"type",
                must_send:false,
                must_save:true,
                type:"hidden",
                placeholder:"",
                value:"database_table"
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
                showName:"table_name ，表名",
                name:"table_name",
                must_send:false,
                must_save:true,
                type:"input",
                placeholder:"数据库表",
                value:""
              },
            
              {
                showName:'字段名称',
                name:"fields",
                must_send:false,
                must_save:false,
                type:"array",
                placeholder:"",
                value:[
                    {
                      "field_1":"id",
                      "field_1_placeholder":"字段名称",
                      "field_2":"id",
                      "field_2_placeholder":"描述",
                      "field_3":"varchar,255",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列长度",
                      "field_4":"primary_key",
                      "field_4_placeholder":"",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    },

                    {
                      "field_1":"name",
                      "field_1_placeholder":"字段名称",
                      "field_2":"名称",
                      "field_2_placeholder":"描述",
                      "field_3":"varchar,255",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列长度",
                      "field_4":"",
                      "field_4_placeholder":"",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    },
                    {
                      "field_1":"create_time",
                      "field_1_placeholder":"字段名称",
                      "field_2":"创建时间",
                      "field_2_placeholder":"描述",
                      "field_3":"varchar,255",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列长度",
                      "field_4":"",
                      "field_4_placeholder":"",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    },
                    
                    {
                      "field_1":"create_by",
                      "field_1_placeholder":"字段名称",
                      "field_2":"创建人",
                      "field_2_placeholder":"描述",
                      "field_3":"varchar,255",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列长度",
                      "field_4":"",
                      "field_4_placeholder":"",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    },
                    {
                      "field_1":"update_time",
                      "field_1_placeholder":"字段名称",
                      "field_2":"创建时间",
                      "field_2_placeholder":"描述",
                      "field_3":"varchar,255",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列长度",
                      "field_4":"",
                      "field_4_placeholder":"",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    },
                    
                    {
                      "field_1":"update_by",
                      "field_1_placeholder":"字段名称",
                      "field_2":"修改人",
                      "field_2_placeholder":"描述",
                      "field_3":"varchar,255",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列长度",
                      "field_4":"",
                      "field_4_placeholder":"",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    },

                    {
                      "field_1":"delete_flag",
                      "field_1_placeholder":"字段名称",
                      "field_2":"是否删除",
                      "field_2_placeholder":"描述",
                      "field_3":"varchar,255",
                      "field_3_placeholder":"以逗号分割 ，第一列类型，第二列长度",
                      "field_4":"",
                      "field_4_placeholder":"",
                      "field_5":"",
                      "field_5_placeholder":"默认值"
                    }

                    
                    ]
              },
              {
                showName:"engine ，引擎",
                name:"engine",
                must_send:false,
                must_save:false,
                type:"input",
                placeholder:"innodb，myisam",
                value:"innodb"
              },
              {
                showName:"init_data_service ，初始化数据",
                name:"init_data_service",
                must_send:false,
                must_save:false,
                type:"input",
                placeholder:"初始化数据",
                value:""
              },
            
              ]
        }
function get_config(){
    return _config
}