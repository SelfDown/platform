var _config = {
	name:"page_info",
	fields:[
			{
			showName:"",
			name:"service_type",
			must_send:false,
			must_save:true,
			type:"hidden",
			placeholder:"",
			value:"service_page_info"
			},
			{
				showName:"",
				name:"type",
				must_send:false,
				must_save:true,
				type:"hidden",
				placeholder:"",
				value:"page_info"
			},
			{
				showName:"service",
				name:"service",
				must_send:true,
				must_save:true,
				type:"input",
				placeholder:"访问路径",
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
				showName:"fields_rules,字段规则。simple_fields：简单字段；form:表单字段，form_field、items_field、rules_field；simple_list2array：数组字field_1转为（字符串型）数组;simple_str2Array:简单类型转为（整形）数组;field_3_2_json_list:将field_3转为json对象数组",
				name:"fields_rules",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"{}",
						"field_3_placeholder":"规则",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},
			{
				showName:"has_operation_items,是否有头部操作项",
				name:"has_operation_items",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"是否有头部操作项",
			},
			{
				showName:"has_operation,是否有列表操作功能",
				name:"has_operation",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"是否有列表操作项",
			},
			{
				showName:"has_search_items,是否有搜索表单",
				name:"has_search_items",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"是否有搜索表单",
			},

			
			{
				showName:"has_right,是否有操作面板",
				name:"has_right",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"是否有操作面板",
			},
			{
				showName:"main_show_type,主面板类型",
				name:"main_show_type",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"主面板类型",
			},
			{
				showName:"add_title,新增标题",
				name:"add_title",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"新增面板框的标题",
			},

			{
				showName:"add_items,新增数据项",
				name:"add_items",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"{}",
						"field_3_placeholder":"规则",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},


			{
				showName:"default_add_items,默认新增数据项",
				name:"default_add_items",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[
					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"{}",
						"field_3_placeholder":"规则",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},
			{
				showName:"add_confirm_button_text,新增面板认文字",
				name:"add_confirm_button_text",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"新增确认文字",
				value:"确认"
			},

			{
				showName:"add_cancel_button_text,新增面板取消文字",
				name:"add_cancel_button_text",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"新增面版取消文字",
				value:"取消"
			},

			{
				showName:"add_msg，新增成功提示",
				name:"add_msg",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"新增成功提示",
			},


			{
				showName:"target_add_service,新增数据处理",
				name:"target_add_service",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"新增数据处理service",
			},

			{
				showName:"edit_title,修改标题",
				name:"edit_title",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"修改面板框的标题",
			},

			{
				showName:"edit_items,修改数据项",
				name:"edit_items",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"{}",
						"field_3_placeholder":"规则",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},

			

			{
				showName:"filters,更新条件筛选字段",
				name:"filters",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"",
						"field_3_placeholder":"",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},

			{
				showName:"edit_confirm_button_text,修改面板确认文字",
				name:"edit_confirm_button_text",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"修改面板确认文字",
				value:"确认"
			},

			{
				showName:"edit_cancel_button_text,修改面板取消文字",
				name:"edit_cancel_button_text",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"修改面板取消文字",
				value:"取消"
			},

			{
				showName:"edit_msg，修改成功提示",
				name:"edit_msg",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"修改成功提示",
			},

			{
				showName:"target_edit_service，修改数据处理",
				name:"target_edit_service",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"修改数据处理service",
			},

			{
				showName:"remove_main_title，标题",
				name:"remove_main_title",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"删除remove_main_title，标题",
			},

			{
				showName:"remove_main_button_text，删除确定",
				name:"remove_main_button_text",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"确定",
			},

			{
				showName:"remove_title，删除提示",
				name:"remove_title",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"删除提示",
			},
			{
				showName:"remove_msg，删除成功提示",
				name:"remove_msg",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"删除提示",

			},

			{
				showName:"remove_filters,删除条件筛选字段",
				name:"remove_filters",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"",
						"field_3_placeholder":"",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},
			{
				showName:"target_remove_service，删除数据处理",
				name:"target_remove_service",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"删除数据处理service",
			},
			{
				showName:"page_title,首部导航栏",
				name:"page_title",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"",
						"field_3_placeholder":"",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},

			{
				showName:"search_items,搜索条件",
				name:"search_items",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"",
						"field_3_placeholder":"",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},

			{
				showName:"form,表单默认值",
				name:"form",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"",
						"field_3_placeholder":"",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},

			{
				showName:"tree_props,树形结果属性",
				name:"tree_props",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"",
						"field_3_placeholder":"",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},

			{
				showName:"tree_node_key,树形结构的key",
				name:"tree_node_key",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"树形结构的key",
				value:"id"
			},

			{
				showName:"tree_node_parent_key,上级节点字段",
				name:"tree_node_parent_key",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"上级节点字段",
				value:"parent_id"
			},

			{
				showName:"tree_current_node_key,默认选中节点",
				name:"tree_current_node_key",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"默认选中节点",
				value:""
			},
			{
				showName:"tree_default_expanded_keys,默认展开节点key",
				name:"tree_default_expanded_keys",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"默认展开节点key"
				
			},

			

			{
				showName:"search_service，查询服务",
				name:"search_service",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"查询服务",
			}

			,
			{
				showName:"search_button_name，搜索按钮文字",
				name:"search_button_name",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"搜索按钮文字",
				value:"搜索"
			}

			,
			{
				showName:"reset_button_name，重置按钮文字",
				name:"reset_button_name",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"重置按钮文字",
				value:"重置"
			}

			,
			{
				showName:"data_loading_tip，数据加载提示",
				name:"data_loading_tip",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"数据加载提示",
				value:"数据拼命加载中"
			}

			,
			{
				showName:"page_size_array，分页数",
				name:"page_size_array",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"分页数",
				value:"10,20,30,40"
			}

			,
			{
				showName:"operation_name,操作列名称",
				name:"operation_name",
				must_send:false,
				must_save:true,
				type:"input",
				placeholder:"操作列名称",
				value:"操作"
			},



			{
				showName:"operation_items,操作项",
				name:"operation_items",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"",
						"field_3_placeholder":"",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},

			{
				showName:"toolbar_button,头部操作面板按钮",
				name:"toolbar_button",
				must_send:false,
				must_save:true,
				type:"array",
				placeholder:"",
				value:[

					{
						"field_1":"",
						"field_1_placeholder":"字段",
						"field_2":"",
						"field_2_placeholder":"描述",
						"field_3":"",
						"field_3_placeholder":"",
						"field_4":"",
						"field_4_placeholder":"",
						"field_5":"",
						"field_5_placeholder":"默认值"
					}
				
				]
			},
			

			
			




		]
}
function get_config(){
    return _config
}