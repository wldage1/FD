package com.sw.util.thread;

import java.util.TimerTask;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.sw.core.data.dao.IDao;
import com.sw.core.data.entity.RelationEntity;


public class FileConvertService extends TimerTask {

	private IDao<RelationEntity> relationDao;
	private SimpleAsyncTaskExecutor asyncTaskExecutor;
	private ThreadPoolTaskExecutor poolTaskExecutor;
	
	@Override
	public void run(){
		//poolTaskExecutor.submit(new ScheduelTask());
	}


	public void runTask(IDao<RelationEntity> relationDao){
		asyncTaskExecutor.submit(new ScheduelTask(relationDao));
	}

	public void setPoolTaskExecutor(ThreadPoolTaskExecutor poolTaskExecutor) {
		this.poolTaskExecutor = poolTaskExecutor;
	}


	public ThreadPoolTaskExecutor getPoolTaskExecutor() {
		return poolTaskExecutor;
	}


	public IDao<RelationEntity> getRelationDao() {
		return relationDao;
	}


	public void setRelationDao(IDao<RelationEntity> relationDao) {
		this.relationDao = relationDao;
	}

	public SimpleAsyncTaskExecutor getAsyncTaskExecutor() {
		return asyncTaskExecutor;
	}


	public void setAsyncTaskExecutor(SimpleAsyncTaskExecutor asyncTaskExecutor) {
		this.asyncTaskExecutor = asyncTaskExecutor;
	}

}
