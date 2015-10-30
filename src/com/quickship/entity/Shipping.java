package com.quickship.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * �˵�
 * @author it-1
 *
 */
@Entity
@Table(name = "qs_shipping")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "qs_shipping_sequence")
public class Shipping extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5142358742060019695L;
	
	
	/** �������� */
	public enum ServerType{
		FIRST_OVERNIGHT,
		PRIORITY_OVERNIGHT,
		STANDARD_OVERNIGHT,
		FEDEX_2_DAY,
		FEDEX_2_DAY_AM,
		FEDEX_EXPRESS_SAVER,
		
		FEDEX_GROUND,
		GROUND_HOME_DELIVERY
		
//		FEDEX_FIRST_FREIGHT,
//		FEDEX_1_DAY_FREIGHT,
//		FEDEX_2_DAY_FREIGHT,
//		FEDEX_3_DAY_FREIGHT
		
	}
//	public enum ExpressType{
//		PRIORITY_OVERNIGHT,
//		STANDARD_OVERNIGHT,
//		FEDEX_2_DAY_AM,
//		FEDEX_2_DAY,
//		FEDEX_EXPRESS_SAVER,
//	}
	
	/** ��װ���� */
	public enum PackageType{
		FEDEX_BOX,
		FEDEX_ENVELOPE,
		FEDEX_TUBE,
		FEDEX_PAK,
		YOUR_PACKAGING
	}
	
	/** Drop Off���� */
	public enum DropoffType{
		REGULAR_PICKUP,
		DROP_BOX
	}
	
	/**����״̬*/
	public enum ReconcileType{
		/**û�е��뱨��*/
		NOREADY, 
		/**�Ѿ����뱨��*/
		READY,
		/**��ɵ���*/
		COMPLETED
	}
	
	
	
	/** �������� */
	private ServerType serverType;
	
	/** fedex�������� */
	private String fedexServerType;
	
	/** ��װ���� */
	private PackageType pakageType;
	
	/** Drop Off���� */
	private DropoffType dropoffType;
	
	/** Դ��ַ */
	private Address oriAddress;
	
	/** Ŀ���ַ */
	private Address desAddress;
	
	/** �������� */
	private List<ShippingItem> shipItems = new ArrayList<ShippingItem>();
	
	/** ���� */
	private BigDecimal weight;
	
	/** ʵ�շ��� =��������+���ӷ���-�ͻ��ۿ۷� */
	private BigDecimal netCharge;
	
	/** �������� */
	private BigDecimal baseCharge;
	
	/** ���ӷ� */
	private BigDecimal surCharge;
	
	/** �ɱ� =��������+���ӷ���-fedex�ۿ۷�*/
	private BigDecimal costCharge;
	
	/** �ͻ��ۿ۷� */
	private BigDecimal memberDiscount;
	
	/** fedex�ۿ۷� */
	private BigDecimal fedexDiscount;
	
	/** ��������*/
	private Date deliveryDate;
	
	/** ��ǩ�ļ�·�� */
	private String labelPath;
	
	/** ��ǩ���� */
	private String labelTrackNo;
	
	/** ��Ա */
	private Member member;
	
	/** �Ƿ񱣴泣�� */
	private Boolean isSeaved;
	
	/** ���ñ��� */
	private String byName;
	
	/** ����������*/
	private Shipping parent;
	
	/** �ӵķ����� */
	private List<Shipping> childShippings = new ArrayList<Shipping>();
	
	/**ʵ�ʷ�������*/
	private BigDecimal factWeight;
	
	/**ʵ�շ���*/
	private BigDecimal factCharge;
	
	/** �Ƿ����*/
	private ReconcileType reconcileType;
	
	@JsonProperty
	public ServerType getServerType() {
		return serverType;
	}

	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}
	
	
	public String getFedexServerType() {
		return fedexServerType;
	}

	public void setFedexServerType(String fedexServerType) {
		this.fedexServerType = fedexServerType;
	}

	@JsonProperty
	public PackageType getPakageType() {
		return pakageType;
	}

	public void setPakageType(PackageType pakageType) {
		this.pakageType = pakageType;
	}
	@JsonProperty
	public DropoffType getDropoffType() {
		return dropoffType;
	}
	
	public void setDropoffType(DropoffType dropoffType) {
		this.dropoffType = dropoffType;
	}
	
	@JsonProperty
	@OneToOne
    @JoinColumn(name="oriAddress")  
	public Address getOriAddress() {
		return oriAddress;
	}

	public void setOriAddress(Address oriAddress) {
		this.oriAddress = oriAddress;
	}
	@JsonProperty
	@OneToOne  
    @JoinColumn(name="desAddress")  
	public Address getDesAddress() {
		return desAddress;
	}

	public void setDesAddress(Address desAddress) {
		this.desAddress = desAddress;
	}
	
	@JsonProperty
	@OneToMany(mappedBy="shipping",fetch=FetchType.EAGER,cascade=CascadeType.PERSIST)
	public List<ShippingItem> getShipItems() {
		return shipItems;
	}

	public void setShipItems(List<ShippingItem> shipItems) {
		this.shipItems = shipItems;
	}

	public BigDecimal getNetCharge() {
		return netCharge;
	}

	public void setNetCharge(BigDecimal netCharge) {
		this.netCharge = netCharge;
	}

	public BigDecimal getBaseCharge() {
		return baseCharge;
	}

	public void setBaseCharge(BigDecimal baseCharge) {
		this.baseCharge = baseCharge;
	}

	public BigDecimal getSurCharge() {
		return surCharge;
	}

	public void setSurCharge(BigDecimal surCharge) {
		this.surCharge = surCharge;
	}

	public BigDecimal getCostCharge() {
		return costCharge;
	}

	public void setCostCharge(BigDecimal costCharge) {
		this.costCharge = costCharge;
	}

	
	public BigDecimal getMemberDiscount() {
		return memberDiscount;
	}

	public void setMemberDiscount(BigDecimal memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public BigDecimal getFedexDiscount() {
		return fedexDiscount;
	}

	public void setFedexDiscount(BigDecimal fedexDiscount) {
		this.fedexDiscount = fedexDiscount;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getLabelPath() {
		return labelPath;
	}

	public void setLabelPath(String labelPath) {
		this.labelPath = labelPath;
	}

	public String getLabelTrackNo() {
		return labelTrackNo;
	}

	public void setLabelTrackNo(String labelTrackNo) {
		this.labelTrackNo = labelTrackNo;
	}
	@ManyToOne(fetch=FetchType.EAGER)
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Boolean getIsSeaved() {
		return isSeaved;
	}

	public void setIsSeaved(Boolean isSeaved) {
		this.isSeaved = isSeaved;
	}
	
	public String getByName() {
		return byName;
	}

	public void setByName(String byName) {
		this.byName = byName;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	public Shipping getParent() {
		return parent;
	}

	public void setParent(Shipping parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy="parent",fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)
	public List<Shipping> getChildShippings() {
		return childShippings;
	}

	public void setChildShippings(List<Shipping> childShippings) {
		this.childShippings = childShippings;
	}
	
	public BigDecimal getFactWeight() {
		return factWeight;
	}

	public void setFactWeight(BigDecimal factWeight) {
		this.factWeight = factWeight;
	}

	public BigDecimal getFactCharge() {
		return factCharge;
	}

	public void setFactCharge(BigDecimal factCharge) {
		this.factCharge = factCharge;
	}

	public ReconcileType getReconcileType() {
		return reconcileType;
	}

	public void setReconcileType(ReconcileType reconcileType) {
		this.reconcileType = reconcileType;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	@Transient
	public BigDecimal getWeight(){
		this.weight = new BigDecimal(0);
		if(shipItems!=null&&shipItems.size()>0){
			for(ShippingItem shipItem:shipItems){
				weight=shipItem.getWeight().add(weight);
			}
		}
		return weight;
	}
	
	@Transient
	public BigDecimal getInsured(){
		BigDecimal insured = new BigDecimal(0);
		if(shipItems!=null&&shipItems.size()>0){
			for(ShippingItem shipItem:shipItems){
				insured=shipItem.getInsured().add(insured);
			}
		}
		return insured;
	}
	@Transient
	public BigDecimal getTotalNetCharge(){
		BigDecimal netCharge = new BigDecimal(0);
		if(shipItems!=null&&shipItems.size()>0){
			for(ShippingItem shipItem:shipItems){
				netCharge=shipItem.getNetCharge().add(netCharge);
			}
		}
		return netCharge;
	}
	@Transient
	public BigDecimal getTotalBaseCharge(){
		BigDecimal baseCharge = new BigDecimal(0);
		if(shipItems!=null&&shipItems.size()>0){
			for(ShippingItem shipItem:shipItems){
				baseCharge=shipItem.getBaseCharge().add(baseCharge);
			}
		}
		return baseCharge;
	}
	@Transient
	public BigDecimal getTotalSurCharge(){
		BigDecimal surCharge = new BigDecimal(0);
		if(shipItems!=null&&shipItems.size()>0){
			for(ShippingItem shipItem:shipItems){
				surCharge=shipItem.getSurCharge().add(surCharge);
			}
		}
		return surCharge;
	}
	@Transient
	public BigDecimal getDifferenceCharge(){
		if(getFactCharge()!=null&&getNetCharge()!=null){
			return getFactCharge().subtract(getNetCharge());
		}
		return new BigDecimal(0);
	}

}
