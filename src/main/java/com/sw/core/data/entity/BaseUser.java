package com.sw.core.data.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class BaseUser extends RelationEntity implements UserDetails {
	
	private static final long serialVersionUID = -5132612672760878653L;
	//spring安全鉴权验证必要属性，用户账号
    private String username;
    /** 用户密码：用户登录系统的验证密码。 */
    private String password;
    //核心代码调用属性，访问ip地址
    private String accessIp;
    //核心代码调用属性，系统样式目录
    private String style;
    /**操作日志内容*/
    private String content;
    
    private Collection<GrantedAuthority> grantedAuthority = null;
	
	public Collection<GrantedAuthority> getGrantedAuthority() {
		return grantedAuthority;
	}

	public void setGrantedAuthority(Collection<GrantedAuthority> grantedAuthority) {
		this.grantedAuthority = grantedAuthority;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
	    return  grantedAuthority;
	}
   
	public void setAuthorities(Collection<GrantedAuthority> grantedAuthority) {
		this.grantedAuthority = grantedAuthority;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccessIp() {
		return accessIp;
	}
	public void setAccessIp(String accessIp) {
		this.accessIp = accessIp;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}