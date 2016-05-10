package com.sw.plugins.market.order.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import com.sw.core.initialize.InitData;
import com.sw.core.service.CommonService;
import com.sw.plugins.market.order.entity.TaxRate;

/**
 * 税Service
 * @author liushuai
 *
 */
@Service
public class TaxRateService extends CommonService<TaxRate>{

	@Override
	public void save(TaxRate entity) throws Exception {
		super.getRelationDao().insert("taxrate.insert", entity);
	}

	@Override
	public void update(TaxRate entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Long getRecordCount(TaxRate entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TaxRate> getList(TaxRate entity) throws Exception {
		return (List<TaxRate>) super.getRelationDao().selectList("taxrate.select", entity);
	}

	@Override
	public List<TaxRate> getPaginatedList(TaxRate entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(TaxRate entity) throws Exception {
		super.getRelationDao().delete("taxrate.delete", entity);
	}

	@Override
	public void deleteByArr(TaxRate entity) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaxRate getOne(TaxRate entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getGrid(TaxRate entity) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String upload(TaxRate entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object download(TaxRate entity, HttpServletRequest request)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void init(InitData initData) throws Exception {
		String path =Thread.currentThread().getContextClassLoader().getResource("").getPath().replace("%20", " ") + "taxRate.xml";
		File templateXML = new File(path);
		SAXReader saxReader = new SAXReader();
		// 把文件读入到文档
		Document document = saxReader.read(templateXML);
		// 获取文档根节点
		Element root = document.getRootElement();//获得根节点
		Element element;
		// 清除数据
		this.delete(new TaxRate());
		for(Iterator iter = root.elementIterator();iter.hasNext();){
			//获得具体的row元素    
			element = (Element)iter.next();  
            //最大应纳所得税额度阀值
            String maxPersonalIncomeTax = element.elementText("maxPersonalIncomeTax");
            //最小应纳所得税额度阀值
            String minPersonalIncomeTax = element.elementText("minPersonalIncomeTax");
            //适用税率
            String taxRate = element.elementText("taxRate");
            //速算扣除数
            String deductCount = element.elementText("deductCount");
            TaxRate entity = new TaxRate();
            if(maxPersonalIncomeTax != null){
            	entity.setMaxPersonalIncomeTax(BigDecimal.valueOf(Double.parseDouble(maxPersonalIncomeTax)));
            }
            if(minPersonalIncomeTax != null){
            	entity.setMinPersonalIncomeTax(BigDecimal.valueOf(Double.parseDouble(minPersonalIncomeTax)));
            }
            entity.setTaxRate(BigDecimal.valueOf(Double.parseDouble(taxRate)));
            entity.setDeductCount(BigDecimal.valueOf(Double.parseDouble(deductCount)));
            this.save(entity);
		}
	}

	
}
