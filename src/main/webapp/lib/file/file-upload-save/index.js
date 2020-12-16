var _config = {
      name:"file_upload_save",
      fields:[
          {
          showName:"",
          name:"service_type",
          must_send:false,
          must_save:true,
          type:"hidden",
          placeholder:"",
          value:"service_file_upload_save"
          },
          {
            showName:"",
            name:"type",
            must_send:false,
            must_save:true,
            type:"hidden",
            placeholder:"",
            value:"file_upload_save"
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
            showName:"accept ，文件格式",
            name:"accept",
            must_send:false,
            must_save:true,
            type:"input",
            placeholder:"以逗号分割，例如.jpg,.png,.gif,.wlkx",
            value:""
          },
          {
            showName:"max_size ，文件大小（M）",
            name:"max_size",
            must_send:false,
            must_save:false,
            type:"input",
            placeholder:"文件大小，例如500",
            value:""
          },
        
          
          {
            showName:"process_data_service ,处理数据服务",
            name:"process_data_service",
            must_send:false,
            must_save:true,
            type:"input",
            placeholder:"处理数据服务",
            value:""
          },

          {
            showName:'保存根目录',
            name:"root",
            must_send:false,
            must_save:false,
            type:"input",
            placeholder:"",
            value:"C:/file/files/"
          },

          {
            showName:'保存目录规则',
            name:"path",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"",
            value:[
              
                {
                  "field_1":"day",
                  "field_1_placeholder":"日期",
                  "field_2":"",
                  "field_2_placeholder":"",
                  "field_3":"day",
                  "field_3_placeholder":"规则列 day 获取日期，当前count累加 除以1000后的值,user_id_4",
                  "field_4":"",
                  "field_4_placeholder":"",
                  "field_5":"",
                  "field_5_placeholder":"默认值"
                },
                {
                  "field_1":"count",
                  "field_1_placeholder":"计数",
                  "field_2":"",
                  "field_2_placeholder":"",
                  "field_3":"count",
                  "field_3_placeholder":"规则列 day 获取日期，当前count累加 除以1000后的值",
                  "field_4":"",
                  "field_4_placeholder":"",
                  "field_5":"1000",
                  "field_5_placeholder":"默认值"
                },

                {
                  "field_1":"user_id_4",
                  "field_1_placeholder":"日期",
                  "field_2":"",
                  "field_2_placeholder":"",
                  "field_3":"user_id_4",
                  "field_3_placeholder":"规则列 day 获取日期，当前count累加 除以1000后的值,user_id_4 用户ID的前4位",
                  "field_4":"",
                  "field_4_placeholder":"",
                  "field_5":"",
                  "field_5_placeholder":"默认值"
                },
            ]
          },

          {
            showName:'访问目录前缀，nginx 配置到C:/file',
            name:"domain_path",
            must_send:false,
            must_save:false,
            type:"input",
            placeholder:"",
            value:"/files/"
          },
        

          {
            showName:'生成数据列',
            name:"fields",
            must_send:false,
            must_save:false,
            type:"array",
            placeholder:"",
            value:[
                {
                  "field_1":"code",
                  "field_1_placeholder":"字段",
                  "field_2":"",
                  "field_2_placeholder":"",
                  "field_3":"",
                  "field_3_placeholder":"",
                  "field_4":"",
                  "field_4_placeholder":"",
                  "field_5":"",
                  "field_5_placeholder":"默认值"
                },
              
                {
                  "field_1":"file_name",
                  "field_1_placeholder":"字段",
                  "field_2":"",
                  "field_2_placeholder":"",
                  "field_3":"",
                  "field_3_placeholder":"",
                  "field_4":"",
                  "field_4_placeholder":"",
                  "field_5":"",
                  "field_5_placeholder":"默认值"
                },
                {
                  "field_1":"file_index",
                  "field_1_placeholder":"日期",
                  "field_2":"",
                  "field_2_placeholder":"",
                  "field_3":"",
                  "field_3_placeholder":"",
                  "field_4":"",
                  "field_4_placeholder":"",
                  "field_5":"",
                  "field_5_placeholder":"默认值"
                },

                {
                  "field_1":"file_source_path",
                  "field_1_placeholder":"字段",
                  "field_2":"",
                  "field_2_placeholder":"",
                  "field_3":"",
                  "field_3_placeholder":"",
                  "field_4":"",
                  "field_4_placeholder":"",
                  "field_5":"",
                  "field_5_placeholder":"默认值"
                },

                {
                  "field_1":"file_domain_path",
                  "field_1_placeholder":"字段",
                  "field_2":"",
                  "field_2_placeholder":"",
                  "field_3":"",
                  "field_3_placeholder":"",
                  "field_4":"",
                  "field_4_placeholder":"",
                  "field_5":"",
                  "field_5_placeholder":"默认值"
                }
            ]
          }
          ]
    }
function get_config(){
    return _config
}