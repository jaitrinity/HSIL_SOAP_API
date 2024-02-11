package com.hsil.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="`Employee-Master`")
public class EmployeeMasterModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="`Eid`")
	private Integer eId;
	
	@Column(name="`Emp-id`")
	private String empId;
	
	@Column(name="`OrgID`")
	private Integer orgId;
	
	@Column(name="`Ecode`")
	private Integer eCode;
	
	@Column(name="`Name`")
	private String name;
	
	@Column(name="`Mobile`")
	private String mobile;
	
	@Column(name="`DOJ`")
	@Temporal(TemporalType.DATE)
	private Date doj;
	
	@Column(name="`Role`")
	private String role;
	
	@Column(name="`Role_Code`")
	private String roleCode;
	
	@Column(name="`territory_code`")
	private String territoryCode;
	
	@Column(name="`territory_name`")
	private String territoryName;
	
	@Column(name="`RM-Id`")
	private String rmId;
	
	@Column(name="`Email-id`")
	private String emailId;
	
	@Column(name="`OfficialEmailID`")
	private String officialEmailId;
	
	@Column(name="`OrgnizationName`")
	private String organizationName;
	
	@Column(name="`OrgnizationName_updated`")
	private String organizationNameUpdated;
	
	@Column(name="`LocationName`")
	private String locationName;
	
	@Column(name="`LocationCode`")
	private String locationCode;
	
	@Column(name="`GradeTitle`")
	private String gradeTitle;
	
	@Column(name="`DepartmentName`")
	private String departmentName;
	
	@Column(name="`BussinessUnit`")
	private String bussinessUnit;
	
	@Column(name="`Designation`")
	private String designation;
	
	@Column(name="`ReportingEmpMail`")
	private String reportingEmpMail;
	
	@Column(name="`Password`")
	private String password;
	
	@Column(name="`Status`")
	private String status;
	
	@Column(name="`active`")
	private Integer active;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="`Create_Date`")
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="`Last_Update_Date`")
	private Date lastUpdateDate;

	public Integer geteId() {
		return eId;
	}

	public void seteId(Integer eId) {
		this.eId = eId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public Integer geteCode() {
		return eCode;
	}

	public void seteCode(Integer eCode) {
		this.eCode = eCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getTerritoryCode() {
		return territoryCode;
	}

	public void setTerritoryCode(String territoryCode) {
		this.territoryCode = territoryCode;
	}

	public String getTerritoryName() {
		return territoryName;
	}

	public void setTerritoryName(String territoryName) {
		this.territoryName = territoryName;
	}

	public String getRmId() {
		return rmId;
	}

	public void setRmId(String rmId) {
		this.rmId = rmId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getOfficialEmailId() {
		return officialEmailId;
	}

	public void setOfficialEmailId(String officialEmailId) {
		this.officialEmailId = officialEmailId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getGradeTitle() {
		return gradeTitle;
	}

	public void setGradeTitle(String gradeTitle) {
		this.gradeTitle = gradeTitle;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getBussinessUnit() {
		return bussinessUnit;
	}

	public void setBussinessUnit(String bussinessUnit) {
		this.bussinessUnit = bussinessUnit;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getReportingEmpMail() {
		return reportingEmpMail;
	}

	public void setReportingEmpMail(String reportingEmpMail) {
		this.reportingEmpMail = reportingEmpMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getOrganizationNameUpdated() {
		return organizationNameUpdated;
	}

	public void setOrganizationNameUpdated(String organizationNameUpdated) {
		this.organizationNameUpdated = organizationNameUpdated;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public Date getDoj() {
		return doj;
	}

	public void setDoj(Date doj) {
		this.doj = doj;
	}
	
	
	
	
}
