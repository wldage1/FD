package com.sw.plugins.market.bankwater.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import jxl.write.WritableWorkbook;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.incesoft.tools.excel.ExcelRowIterator;
import com.incesoft.tools.excel.ReaderSupport;
import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.bankwater.entity.PayConfirmFromProvider;
import com.sw.plugins.product.manage.entity.Product;
import com.sw.plugins.product.manage.service.ProductService;
import com.sw.util.ExcelReadUtil;
import com.sw.util.ExportExcel;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

public class BankWaterService extends CommonService<PayConfirmFromProvider>{
	
	@Resource
	private ProductService productService;
	
	@Override
	public Object download(PayConfirmFromProvider entity,HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public void init(InitData initData) throws Exception {
	}
	@Override
	public void deleteByArr(PayConfirmFromProvider entity) throws Exception {
	}
	@Override
	public List<PayConfirmFromProvider> getList(PayConfirmFromProvider entity) throws Exception {
		return null;
	}
	
	@Override
	public void save(PayConfirmFromProvider entity) throws Exception {
		getRelationDao().insert("bankWater.insert", entity);
	}

	@Override
	public void update(PayConfirmFromProvider entity) throws Exception {
		super.getRelationDao().update("bankWater.update_mappingbymanual", entity);
	}

	@Override
	public Long getRecordCount(PayConfirmFromProvider entity) throws Exception {
		return super.getRelationDao().getCount("bankWater.select_count", entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PayConfirmFromProvider> getPaginatedList(PayConfirmFromProvider entity) throws Exception {
		return (List<PayConfirmFromProvider>) super.getRelationDao().selectList("bankWater.select_paginatedList", entity);
	}

	@Override
	public void delete(PayConfirmFromProvider entity) throws Exception {
		super.getRelationDao().delete("bankWater.delete", entity);
	}

	@Override
	public PayConfirmFromProvider getOne(PayConfirmFromProvider entity) throws Exception {
		return (PayConfirmFromProvider) super.getRelationDao().selectOne("bankWater.select_one", entity);
	}
	
	/**
	 * 上传文件到服务器临时文件夹
	 */
	public Map<String,String> uploadFile(PayConfirmFromProvider payConfirmFromProvider, HttpServletRequest request) throws Exception {
		Map<String,String> returnMap=new HashMap<String, String>();
		//FTP上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");
		
		String uploadPath = request.getSession().getServletContext().getRealPath("/") + "upload";
		File uploadFilePath=new File(uploadPath);
		if(!uploadFilePath.exists()){
			uploadFilePath.mkdir();
		}
		String localPath = request.getSession().getServletContext().getRealPath("/") + "upload/bankwaterImport";
		String tempFileName = request.getParameter("Filename");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		String fileName = FTPUtil.getRandomString() + fileExtensionName;
		File localFilePath = new File(localPath);
		if (!localFilePath.exists()) {
			localFilePath.mkdir();
		}
		File localFile = new File(localPath, fileName);
		byte[] bytes = file.getBytes();
		if (bytes.length != 0) {
			file.transferTo(localFile);
		}
		//验证 文件格式
		ReaderSupport readerSupport = null ;
		if (fileName.endsWith(".xls")) {
			readerSupport =  ReaderSupport.newInstance(ReaderSupport.TYPE_XLS, localFile);
		} else if(fileName.endsWith(".xlsx")) {
			readerSupport =  ReaderSupport.newInstance(ReaderSupport.TYPE_XLSX, localFile);
		}
		if(readerSupport != null) {
			try{
				readerSupport.open() ;
				ExcelRowIterator it = readerSupport.rowIterator();
				if(!it.nextRow()){
					returnMap.put("fileError", "文件行有误！");
				}else{
					if(it.getCellCount()!=3){
						returnMap.put("fileError", "文件列有误！");
					}else{
						if(!it.getCellValue(0).trim().equals("姓名")){
							returnMap.put("fileError", "文件第一列应为姓名列！");
						}else if(!it.getCellValue(1).trim().equals("打款金额")){
							returnMap.put("fileError", "文件第二列应为打款金额列！");
						}else if(!it.getCellValue(2).trim().equals("打款时间")){
							returnMap.put("fileError", "文件第三列应为打款时间列！");
						}else{
							boolean isGoto=true;
							while(it.nextRow() && isGoto){
								for(int i = 0; i < it.getCellCount(); i ++) {
									if(it.getCellValue(i)==null || it.getCellValue(i).equals("")){
										returnMap.put("fileError", "文件中不能有空列！");
										isGoto=false;
										break;
									}else{
										if(i==1){
											try{
												BigDecimal.valueOf(Double.valueOf(it.getCellValue(1)));
											}catch (Exception e) {
												returnMap.put("fileError", "文件中金额列不能有非数字类型数据！");
												isGoto=false;
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}catch (Exception e) {
				
			}
		}	
		returnMap.put("fileUrl", localPath + "/" + fileName);
		return returnMap;
	}
	
	/**
	 * 上传文件到服务器临时文件夹
	 */
	@Override
	public String upload(PayConfirmFromProvider payConfirmFromProvider, HttpServletRequest request) throws Exception {
		return null;
	}
	
	/**
	 * 银行流水导入数据库    ---同时文件上传FTP
	 * @param payConfirmFromProvider
	 * @param filePath
	 * @throws Exception
	 */
	public void bankWaterImport(PayConfirmFromProvider payConfirmFromProvider ,String filePath) throws Exception{
		//根据产品类型和产品状态进行 确认是否删除 流水记录  私募和货币基金型产品 申购时不需要删除  其余全部删除
		Product product=new Product();
		product.setId(payConfirmFromProvider.getProductId());
		product=productService.getOne(product);
		if(product.getType().equals((short)2) || product.getType().equals((short)5)){
			if(!product.getSellStatus().equals((short)5) || !product.getSellStatus().equals((short)6)){
				getRelationDao().delete("bankWater.delete", payConfirmFromProvider);
			}
		}else{
			getRelationDao().delete("bankWater.delete", payConfirmFromProvider);
		}
		List<Map<String, String>> list = ExcelReadUtil.readExcel(filePath, true);
		for (Map<String, String> map : list) {
			if((map.get("col1")==null || map.get("col1").equals("")) && (map.get("col2")==null || map.get("col2").equals("")) && map.get("col3")==null || map.get("col3").equals("")){
				break;
			}
			PayConfirmFromProvider tempPayConfirm = payConfirmFromProvider;
			tempPayConfirm.setName(map.get("col1"));
			tempPayConfirm.setPayAmount(BigDecimal.valueOf(Double.valueOf((map.get("col2")==null || map.get("col2").equals(""))? "0" :map.get("col2"))));
			tempPayConfirm.setPayTime(map.get("col3"));
			tempPayConfirm.setMatchingStatus(0);
			save(tempPayConfirm);
		}
		//上传FTP
		File file = new File(filePath);
		String path = SystemProperty.getInstance("config").getProperty("bankwater.import.path") + String.valueOf(payConfirmFromProvider.getProductId() + "/" + System.currentTimeMillis() + "/");
		String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
		FTPUtil.uploadFile(file, path, fileName);
	}
	
	/**
	 * 通过认购/申购的产品
	 * @param payConfirmFromProvider
	 * @return
	 * @throws Exception
	 */
	public List<Product> getProductList(PayConfirmFromProvider payConfirmFromProvider) throws Exception{
		List<Product> productList = null;
		Product product=new Product();
		product.setOrgId(payConfirmFromProvider.getOrgID());
		if(payConfirmFromProvider.getDataType()==1){
			productList = productService.getOfferPayList(product);
		}else if(payConfirmFromProvider.getDataType()==2){
			productList = productService.getSubPayList(product);
		}
		return productList;
	}
	
	/**
	 * 银行流水导出
	 * 
	 * @param c
	 * @param businessTypeID
	 * @param colName
	 * @param colModel
	 * @param excel
	 * @param payConfirmFromProvider
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public WritableWorkbook exportExcel(String c, String businessTypeID, String colName, String colModel, WritableWorkbook excel, PayConfirmFromProvider payConfirmFromProvider,Map<String, Object> matchingStatusMap) throws Exception {	
		List<Map<String, Object>> objList = (List<Map<String, Object>>) super.getRelationDao().selectList("bankWater.export_excel",payConfirmFromProvider);
		if (objList != null) {
			for (Map<String, Object> one : objList) {
				//TSR姓名
				one.put("provider.shortName",one.get("PROVIDERSHORTNAME"));
				one.put("product.shortName",one.get("PRODUCTSHORTNAME"));
				one.put("name",one.get("NAME"));
				one.put("payAmount",one.get("PAYAMOUNT"));
				one.put("payTime",one.get("PAYTIME"));
				one.put("matchingStatus",matchingStatusMap.get(String.valueOf(one.get("MATCHINGSTATUS"))));
			}
		}
		String[] colNames = colName.split(",");
		String[] colModels = colModel.split(",");
		
		String excelName = "银行流水统计";
		int colNameNum = colNames.length ;
		int colModelNum = colModels.length;
		ExportExcel exportExcel = new ExportExcel();
		excel = exportExcel.createExcel(excelName, colNames, colNameNum, colModels, colModelNum, objList, excel);
		return excel;
	}
	
	@Override
	public Map<String, Object> getGrid(PayConfirmFromProvider entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<PayConfirmFromProvider> resultList = getPaginatedList(entity);
		Long record = getRecordCount(entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}

	/**
	 * 自动匹配 银行流水与凭证
	 * @param payConfirmFromProvider
	 * @throws Exception
	 */
	public void updateMappingBankWater(PayConfirmFromProvider payConfirmFromProvider) throws Exception{
		//多次匹配需要进行判断 之前是否匹配过 如果匹配过 将原有的匹配结果清楚  重新进行匹配
		Long count=super.getRelationDao().getCount("bankWater.select_mapping_count", payConfirmFromProvider);
		if(count>0){
			//重置匹配状态
			super.getRelationDao().update("bankWater.update_matchingStatus", payConfirmFromProvider);
		}
		//匹配数据 包含流水ID 和 资金到账确认ID
		super.getRelationDao().update("bankWater.update_mappingBankWater", payConfirmFromProvider);
	}
	
	/**
	 * 流水与打款凭证 进行手动匹配
	 * @param payConfirmFromProvider
	 * @throws Exception 
	 */
	public void updateByManual(PayConfirmFromProvider payConfirmFromProvider) throws Exception {
		this.update(payConfirmFromProvider);
	}
	
	/**
	 * 获取产品对应 银行流水和打款凭证的对账详情信息
	 * @param payConfirmFromProvider
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> getProductOrderProof(PayConfirmFromProvider payConfirmFromProvider) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		//查询某一产品下的 订单状态为（资金确认/资金确认&单证归集）的订单数量 
		Long orderCount=super.getRelationDao().getCount("bankWater.select_orderCountByProductID", payConfirmFromProvider);
		//查询某一产品下的 订单状态为（资金确认/资金确认&单证归集）的已处理(不代表已对账)的打款凭证数量 
		Long orderProofCount=super.getRelationDao().getCount("bankWater.select_orderProofCountByProductID", payConfirmFromProvider);
		//查询某一产品下的银行流水总数
		Long bankWaterCount=super.getRelationDao().getCount("bankWater.select_bankWaterCountByProductID", payConfirmFromProvider);
		//查询某一产品下的已匹配的银行流水总数
		Long bankWaterHasDisposeCount=super.getRelationDao().getCount("bankWater.select_bankWaterHasDisposeCountByProductID", payConfirmFromProvider);
		//为匹配的银行流水总数
		Long bankWaterHasNotDisposeCount=bankWaterCount-bankWaterHasDisposeCount;
		//为匹配的打款凭证总数
		Long orderProofHasNotDisposeCount=orderProofCount-bankWaterHasDisposeCount;
		map.put("orderCount", orderCount);
		map.put("orderProofCount", orderProofCount);
		map.put("bankWaterCount", bankWaterCount);
		map.put("bankWaterHasDisposeCount", bankWaterHasDisposeCount);
		map.put("bankWaterHasNotDisposeCount", bankWaterHasNotDisposeCount);
		map.put("orderProofHasNotDisposeCount", orderProofHasNotDisposeCount);
		return map;
	}
	
	/**
	 * 查询 满足匹配条件的流水数据和凭证数据
	 * @param payConfirmFromProvider
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Map<String,Object> selectBankWaterList(PayConfirmFromProvider entity) throws Exception {
		Map<String, Object> map = new Hashtable<String, Object>();
		List<PayConfirmFromProvider> resultList = (List<PayConfirmFromProvider>) super.getRelationDao().selectList("bankWater.select_bankwaterandproof_list", entity);
		Long record = super.getRelationDao().getCount("bankWater.select_bankwaterandproof_count", entity);
		int pageCount = (int) Math.ceil(record / (double) entity.getRows());
		map.put("rows", resultList);
		map.put("page", entity.getPage());
		map.put("total", pageCount);
		map.put("records", record);
		return map;
	}
}
