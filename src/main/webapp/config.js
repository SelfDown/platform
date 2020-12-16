var config = {
	data:{
		message:"自动功能页面测试",
		show:{
				username:"admin",
				password:"e10adc3949ba59abbe56e057f20f883e",
				json_data:"{}",
	  			module:"platform_query",
	  			dataList: [],
	  			fields:[

	  			
	  			],
	  			operation:"",
	  			form_data:{

	  			}
	  	},

	  	loginURL:"/auth/login.do",
	  	//loginURL:"/dict/query",
	  	getDataURL:"/insight/tools/query",
	  	saveDataURL:"/insight/tools/save",
	  	updateDataURL:"/insight/tools/update",
	  	deleteDataURL:"/insight/tools/delete",
	  	serviceResultURL:"insight/service/result",
		functions:[

			{
	  			name:"platform_query",
	  			fields:[
	  					{
	  					showName:"",
	  					name:"service_type",
	  					must_send:false,
	  					must_save:true,
	  					type:"hidden",
	  					placeholder:"",
	  					value:"service_platform_query"
	  					},
		  				{
		  					showName:"",
		  					name:"type",
		  					must_send:false,
		  					must_save:true,
		  					type:"hidden",
		  					placeholder:"",
		  					value:"platform_query"
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
		  					showName:"SQL",
		  					name:"sql",
		  					must_send:false,
		  					must_save:true,
		  					type:"input",
		  					placeholder:"查询sql语句",
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
		  					showName:"toTree,是否转成树形结构，以逗号隔开，如'id,parent_id,childrens'字段，空就是列表,虚拟节点delete_flag=0,id=0 ",
		  					name:"toTree",
		  					must_send:false,
		  					must_save:false,
		  					type:"input",
		  					placeholder:"是否转成树形结构，空就是列表",
		  					value:""
		  				},

		  				{
		  					showName:"搜索字段，between_and为字符串",
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
			  							"field_4":"=",
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
			  							"field_4":"=",
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
		  					showName:"默认搜索字段，between_and为字符串",
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
			  							"field_4":"=",
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
		  					placeholder:"显示字段",
		  					value:[{"field_1":"",
		  							"field_1_placeholder":"字段",
		  							"field_2":"",
		  							"field_2_placeholder":"描述",
		  							"field_3":"",
		  							"field_3_placeholder":"显示:true/false",
		  							"field_4":"",
		  							"field_4_placeholder":"更新:true/false",
		  							"field_5":"",
		  							"field_5_placeholder":"备用",
		  							}
		  							]
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

	  		},
	  	]	
	}
}


function loadJS( url, callback ){
    var script = document.createElement('script'),
    fn = callback || function(){};
    script.type = 'text/javascript';
    //IE
    if(script.readyState){
        script.onreadystatechange = function(){
            if( script.readyState == 'loaded' || script.readyState == 'complete' ){
                script.onreadystatechange = null;
               var config = get_config()
                fn(config);
            }
        };
    }else{
        //其他浏览器
        script.onload = function(){
            var config = get_config()
            fn(config);
        };
    }
    script.src = url;
    document.getElementsByTagName('head')[0].appendChild(script);

}

var modules = ["platform.platform-query-simple",
				"platform.platform-query-template",
				"platform.platform-create",
				"platform.platform-update",
				"platform.platform-login",
				"business.version-iteration",
				"file.file-upload-save",
				"database.database-table",
				"database.database-connection-config",
				"page.page-info",
				"ssh.server-scan",
				"ssh.server-login",
				"ssh.server-ssh"]
var tmp_module = {}
modules.map((module)=>{
	var item = module.split(".").join("/")
    loadJS("/lib/"+item+"/index.js",function(item_config){
        console.log("/lib/"+item+"/index.js")
        console.log(item_config)
        console.log("----------------------------------------")
        tmp_module[module] = item_config
    })
})


window.$config = config

 window.onload = function () {
      modules.map((item)=>{
        window.$config.data.functions.push(tmp_module[item])
      })

 };