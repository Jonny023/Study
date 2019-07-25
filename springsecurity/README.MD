# 权限说明

* `IS_AUTHENTICATED_FULLY` - 登录后访问
* 新增角色时要以`ROLE_`开头，如`ROLE_USER`
* 通过`hasRole`方法判断角色时不能传入前缀`ROLE_`，源码如下：
  
  ```java
  private String defaultRolePrefix = "ROLE_";
  
  public final boolean hasRole(String role) {
		return hasAnyRole(role);
	}

	public final boolean hasAnyRole(String... roles) {
		return hasAnyAuthorityName(defaultRolePrefix, roles);
	}
  ```
> 当然，可以通过`defaultRolePrefix`的方法修改这个前缀
