package njupt.microcredit.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.RequestAware;

import com.opensymphony.xwork2.ActionSupport;

import njupt.microcredit.dao.ICustBasicInfoDao;
import njupt.microcredit.entity.BusinessInfo;
import njupt.microcredit.entity.CompanyInfo;
import njupt.microcredit.entity.CustBasicInfo;
import njupt.microcredit.entity.LegalPersonInfo;
import njupt.microcredit.entity.LoanApplyInfo;
import njupt.microcredit.service.IBusinessInfoService;
import njupt.microcredit.service.ICompanyInfoService;
import njupt.microcredit.service.ICustBasicInfoService;
import njupt.microcredit.service.ILegalPersonInfoService;
import njupt.microcredit.service.ILoanApplyInfoService;

public class LoanCompanyAction extends ActionSupport implements RequestAware{
	
	//客户操作的id
	private Integer custId ;
	/**
	 * 操作客户信息
	 */
	private CustBasicInfo custBasicInfo = new CustBasicInfo();
	public void setCustBasicInfo(CustBasicInfo custBasicInfo) {
		this.custBasicInfo = custBasicInfo;
	}
	public CustBasicInfo getCustBasicInfo() {
		return custBasicInfo;
	}
	/**
	 * 贷款信息
	 */
	private LoanApplyInfo loanApplyInfo ;
	public void setLoanApplyInfo(LoanApplyInfo loanApplyInfo) {
		this.loanApplyInfo = loanApplyInfo;
	}
	public LoanApplyInfo getLoanApplyInfo() {
		return loanApplyInfo;
	}
	
	
	/**
	 * 企业基本信息
	 * @return
	 */
	private CompanyInfo companyInfo;
	public void setCompanyInfo(CompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}
	public CompanyInfo getCompanyInfo() {
		return companyInfo;
	}
	
	
	/**
	 * 法人信息
	 * @return
	 */
	private LegalPersonInfo legalPersonInfo;
	public void setLegalPersonInfo(LegalPersonInfo legalPersonInfo) {
		this.legalPersonInfo = legalPersonInfo;
	}
	public LegalPersonInfo getLegalPersonInfo() {
		return legalPersonInfo;
	}
	/**
	 * 经营信息
	 * @return
	 */
	private BusinessInfo businessInfo;
	public void setBusinessInfo(BusinessInfo businessInfo) {
		this.businessInfo = businessInfo;
	}
	public BusinessInfo getBusinessInfo() {
		return businessInfo;
	}
	

	
	/*********************注入service*********************/
	private ICustBasicInfoService custBasicInfoService;
	public void setCustBasicInfoService(ICustBasicInfoService custBasicInfoService) {
		this.custBasicInfoService = custBasicInfoService;
	}
	
	private ILoanApplyInfoService loanApplyInfoService;
	public void setLoanApplyInfoService(ILoanApplyInfoService loanApplyInfoService) {
		this.loanApplyInfoService = loanApplyInfoService;
	}
	
	private ICompanyInfoService companyInfoService;
	public void setCompanyInfoService(ICompanyInfoService companyInfoService) {
		this.companyInfoService = companyInfoService;
	}
	
	private ILegalPersonInfoService legalPersonInfoService;
	public void setLegalPersonInfoService(ILegalPersonInfoService legalPersonInfoService) {
		this.legalPersonInfoService = legalPersonInfoService;
	}
	
	private IBusinessInfoService businessInfoService;
	public void setBusinessInfoService(IBusinessInfoService businessInfoService) {
		this.businessInfoService = businessInfoService;
	}
	
	/**
	 * 设置企业搜索条件
	 */
	private String publicclient ;
	private String companyinfo ;
	private String loantype ;
	private String applytime1 ;
	private String applytime2 ;

	public String getPublicclient() {
		return publicclient;
	}
	public void setPublicclient(String publicclient) {
		this.publicclient = publicclient;
	}
	public String getCompanyinfo() {
		return companyinfo;
	}
	public void setCompanyinfo(String companyinfo) {
		this.companyinfo = companyinfo;
	}
	public String getLoantype() {
		return loantype;
	}
	public void setLoantype(String loantype) {
		this.loantype = loantype;
	}
	public String getApplytime1() {
		return applytime1;
	}
	public void setApplytime1(String applytime1) {
		this.applytime1 = applytime1;
	}
	public String getApplytime2() {
		return applytime2;
	}
	public void setApplytime2(String applytime2) {
		this.applytime2 = applytime2;
	}
	/**
	 * 1.贷款信息展示
	 */
	public String comList() {
		List<Object[]> companyLoan = loanApplyInfoService.getList();
		
		request.put("companyLoan", companyLoan);
		
		return "listcompany";
	}
	
	/**
	 * 2.添加企业贷款信息，进入添加页面
	 */
	public String viewInsert() {		
		return "insert";
	}
	/**
	 * 2.2 添加贷款-添加贷款
	 */
	public String insert() {
		
		//以当前时间作为表示客户的id
		custId = (int) new Date().getTime();
		
		/**
		 * 2.企业信息操作
		 */
		companyInfo.setCustId(custId);
		companyInfo.setCreateDate(new Date());
		companyInfoService.insert(companyInfo);
		
		/**
		 * 3.法人代表信息操作
		 */
		legalPersonInfo.setCustId(custId);
		legalPersonInfo.setCreateDate(new Date());
		legalPersonInfoService.insert(legalPersonInfo);
		
		/**
		 * 4.企业信息操作
		 */
		businessInfo.setCustId(custId);   // 客户id
		businessInfo.setLegalId(legalPersonInfo.getId());
		businessInfo.setCompanyId(companyInfo.getId());  //企业id
		businessInfo.setLegalId(legalPersonInfo.getId()); //法人id
		businessInfoService.insert(businessInfo);
		
		/**
		 * 5.1. 当前提交的时间就是申请的时间
		 * 5.2. 设置客户的基本信息用于显示
		 */		
		loanApplyInfo.setApplyTime(new Date());	
		loanApplyInfo.setCustName(legalPersonInfo.getCustType());//设置法人姓名
		loanApplyInfo.setCustType(2);   //1.代表个人申请  2.代表企业申请
		loanApplyInfo.setCustId(custId);
		loanApplyInfo.setIdentifyType(legalPersonInfo.getIdentifyType());
		loanApplyInfo.setIdentifyNum(legalPersonInfo.getIdentifyNum());
		loanApplyInfo.setPhoneNum(legalPersonInfo.getPhoneNum());
		loanApplyInfoService.insert(loanApplyInfo);
		
		/**
		 * 1.客户操作信息
		 */
		custBasicInfo.setCustId(custId);
		custBasicInfo.setCustomerType(2);
		custBasicInfo.setCreateDate(new Date());
		custBasicInfo.setCreateOr(2);		
		custBasicInfoService.insert(custBasicInfo);
		return "listAction";
	}

	/**
	 * 查看企业贷款信息
	 * @return
	 */
	public String viewCompanyLoanInfo() {
		
		//查看要要获取的贷款记录的id
		int loanId = loanApplyInfo.getLoanId();
//		System.out.println(loanId);
		
		//根据贷款的主键查询信息
		loanApplyInfo = loanApplyInfoService.selectByPrimaryKey(loanId);
		
		//查询客户的信息
		int custId = loanApplyInfo.getCustId();
		System.out.println(custId);
		
		List<CompanyInfo> companyInfo1 =  companyInfoService.selectByCustId(custId);
//		System.out.println(companyInfo1.get(0).toString());
		if(companyInfo1.size() != 0) {
			companyInfo = companyInfo1.get(0);
		}else {
			companyInfo = null;
		}
		
		List<BusinessInfo> businessInfo1 = businessInfoService.selectByCustId(custId);
		if(businessInfo1.size() != 0) {
			businessInfo = businessInfo1.get(0);
		}else {
			businessInfo = null;
		}

		List<LegalPersonInfo> legalPersonInfo1 = legalPersonInfoService.selectByCustId(custId);
		if(legalPersonInfo1.size() != 0) {
			legalPersonInfo = legalPersonInfo1.get(0);
		}
		
		//查询其它表的信息
		
		return "editCompany";
	}
	/**
	 * 修改贷款信息
	 * @return
	 */
	public String updateCompanyInfo() {
		System.out.println(businessInfo);
		legalPersonInfoService.updateByPrimaryKey(legalPersonInfo);
		businessInfoService.updateByPrimaryKeySelective(businessInfo);
		companyInfoService.updateByPrimaryKey(companyInfo);
		
		//设置贷款部分信息
		loanApplyInfo.setCustName(legalPersonInfo.getCustType());		
		loanApplyInfo.setPhoneNum(legalPersonInfo.getPhoneNum());
		loanApplyInfo.setIdentifyType(legalPersonInfo.getIdentifyType());
		loanApplyInfo.setIdentifyNum(legalPersonInfo.getIdentifyNum());
		loanApplyInfoService.updateByPrimaryKey(loanApplyInfo);
		return "listAction";
	}
	
	/**
	 * 根据企业的条件进行查询信息
	 */
	public String loanSearchInfo() {
		//获取jsp页面传输的内容
//		HttpServletRequest req = ServletActionContext.getRequest();
//		String publicclient = req.getParameter("publicclient").trim();
//		String companyinfo = req.getParameter("companyinfo").trim();
//		String loantype = req.getParameter("loantype").trim();
//		String applytime1 = req.getParameter("applytime1").trim();
//		String applytime2 = req.getParameter("applytime2").trim();
		publicclient = publicclient.trim();
		companyinfo = companyinfo.trim();
		loantype = loantype.trim();
		applytime1 = applytime1.trim();
		applytime2 = applytime2.trim();
		
//		System.out.println(publicclient+":"+companyinfo+":"+loantype+":"+applytime1+":"+applytime2);
		
		List<Object[]> companyLoan = loanApplyInfoService
				.selectCompanyCondition(publicclient, companyinfo, loantype, applytime1, applytime2);
		request.put("companyLoan",companyLoan);
		return "loanSearchList";
	}
	
	
	//接收框架运行时候传入的request对象的map
	private Map<String,Object> request;
	@Override
	public void setRequest(Map<String, Object> request) {
		this.request = request;		
	}

}
