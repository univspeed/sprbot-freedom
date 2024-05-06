package com.cybercloud.sprbotfreedom.platform.base.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果
 * @author liuyutang
 */
public class PageResult<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 总记录数
	 */
	private int totalCount;
	/**
	 * 每页记录数
	 */
	private int pageSize;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 当前页数
	 */
	private int currPage;
	/**
	 * 列表数据
	 */
	private List<T> list;
	
	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public PageResult(List<T> list, int totalCount, int pageSize, int currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	/**
	 * 分页
	 */
	public PageResult(IPage<T> page) {
		this.list = page.getRecords();
		this.totalCount = (int)page.getTotal();
		this.pageSize = (int)page.getSize();
		this.currPage = (int)page.getCurrent();
		this.totalPage = (int)page.getPages();
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * 创建分页结果类
	 * @param list
	 * @param totalCount
	 * @param pageSize
	 * @param currPage
	 * @return
	 * @param <T>
	 */
	public static <T> PageResult create(List<T> list, int totalCount, int pageSize, int currPage){
		return new PageResult(list,	totalCount, pageSize, currPage);
	}

	/**
	 * 创建分页结果类
	 * @param page
	 * @return
	 * @param <T>
	 */
	public static <T> PageResult create(IPage<T> page){
		return new PageResult(page);
	}
}
