package com.quickship.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;




@Entity
@Table(name = "qs_area")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "qs_area_sequence")
public class Area extends OrderEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3386139022757893348L;
	/** 树路径分隔符 */
	private static final String TREE_PATH_SEPARATOR = ",";
	
	/** 名称 */
	private String name;
	
	/** 值 */
	private String value;
	
	/** 全名 */
	private String fullName;
	
	/** 树路径 */
	private String treePath;
	
	/** 上级地址 */
	private Area parent;
	
	/** 下级地址 */
	private Set<Area> childern = new HashSet<Area>();
	
	/** 会员 */
	private Set<Member> members = new HashSet<Member>();
	
	@JsonProperty
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	@JsonProperty
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	@JsonProperty
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getTreePath() {
		return treePath;
	}
	
	public void setTreePath(String treePath) {
		this.treePath = treePath;
	}
	
	@JsonProperty
	@ManyToOne(fetch=FetchType.EAGER)
	public Area getParent() {
		return parent;
	}
	
	public void setParent(Area parent) {
		this.parent = parent;
	}
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@OrderBy("order asc")
	public Set<Area> getChildern() {
		return childern;
	}
	
	public void setChildern(Set<Area> childern) {
		this.childern = childern;
	}
	
	@OneToMany(mappedBy="area",fetch=FetchType.LAZY)
	public Set<Member> getMembers() {
		return members;
	}
	
	public void setMembers(Set<Member> members) {
		this.members = members;
	}
	
	/**
	 * 持久化前处理
	 */
	@PrePersist
	public void prePersist() {
		Area parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
			setTreePath(parent.getTreePath() + parent.getId() + TREE_PATH_SEPARATOR);
		} else {
			setFullName(getName());
			setTreePath(TREE_PATH_SEPARATOR);
		}
	}
	
	/**
	 * 更新前处理
	 */
	@PreUpdate
	public void preUpdate() {
		Area parent = getParent();
		if (parent != null) {
			setFullName(parent.getFullName() + getName());
		} else {
			setFullName(getName());
		}
	}
	
}
