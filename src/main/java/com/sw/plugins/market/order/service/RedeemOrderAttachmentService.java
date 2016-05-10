package com.sw.plugins.market.order.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.RedeemOrder;
import com.sw.plugins.market.order.entity.RedeemOrderAttachment;
import com.sw.util.FTPUtil;
import com.sw.util.SystemProperty;

/**
 * 赎回订单 附件Service
 * @author liushuai
 *
 */
@Service
public class RedeemOrderAttachmentService extends CommonService<RedeemOrderAttachment>{
	@Resource
	private RedeemOrderService redeemOrderService;
	
	@Override
	public void save(RedeemOrderAttachment entity) throws Exception {
		super.getRelationDao().insert("redeemorderAttachment.insert", entity);
	}
	
	/**
	 * 保存数据
	 * @param entity
	 * @throws Exception
	 */
	public void saveRedeemOrderAttachment(RedeemOrderAttachment entity)throws Exception{
		RedeemOrder order = new RedeemOrder();
		order.setTradeStatus((short)3);
		order.setDocumentStatus((short)2);
		order.setId(entity.getId());
		//更改赎回订单交易状态为单证上传
		redeemOrderService.update(order);
		String fileUrl=SystemProperty.getInstance("config").getProperty("redeem.attachment.path")+entity.getOrderNumber()+entity.getFileName().substring(entity.getFileName().lastIndexOf("."));
		entity.setFileUrl(fileUrl);
		//审核状态
		entity.setAudited((short)1);
		entity.setId(null);
		//存赎回订单附件表
		this.save(entity);
	}
	
	/**
	 * 上传附件至FTP
	 * @param entity
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String uploadfile(RedeemOrderAttachment entity, HttpServletRequest request)throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("Filedata");
		String tempFileName = request.getParameter("Filename");
		String fileExtensionName = tempFileName.substring(tempFileName.lastIndexOf("."));
		String path=SystemProperty.getInstance("config").getProperty("redeem.attachment.path");
		String fileName = entity.getOrderNumber()+fileExtensionName;
		return FTPUtil.uploadFile(file, path, fileName);
	}
	@Override
	public void update(RedeemOrderAttachment entity) throws Exception {
	}
	@Override
	public Long getRecordCount(RedeemOrderAttachment entity) throws Exception {
		return null;
	}
	@Override
	public List<RedeemOrderAttachment> getList(RedeemOrderAttachment entity) throws Exception {
		return null;
	}
	@Override
	public List<RedeemOrderAttachment> getPaginatedList( RedeemOrderAttachment entity) throws Exception {
		return null;
	}
	@Override
	public void delete(RedeemOrderAttachment entity) throws Exception {
	}
	@Override
	public void deleteByArr(RedeemOrderAttachment entity) throws Exception {
	}
	@Override
	public RedeemOrderAttachment getOne(RedeemOrderAttachment entity) throws Exception {
		return null;
	}
	@Override
	public Map<String, Object> getGrid(RedeemOrderAttachment entity) throws Exception {
		return null;
	}
	@Override
	public String upload(RedeemOrderAttachment entity, HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public Object download(RedeemOrderAttachment entity,HttpServletRequest request) throws Exception {
		return null;
	}
	@Override
	public void init(InitData initData) throws Exception {
	}
}
