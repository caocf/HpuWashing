package com.edaixi.dataset;

/**
 * 数据集类型必备的接口
 * 
 * @author A Shuai
 * 
 */
public interface IDataSet<E> {

	/**
	 * 向数据集中添加一个数据bean
	 * 
	 * @param mBean
	 */
	public void addBean(E mBean);

	/**
	 * 删除指定索引值的数据项
	 * 
	 * @param mIndex
	 * @return
	 */
	public E removeIndexBean(int mIndex);

	/**
	 * 提取指定索引指定的数据bean
	 * 
	 * @param mIndex
	 * @return
	 */
	public E getIndexBean(int mIndex);

	/**
	 * 数据bean数量
	 * 
	 * @return
	 */
	public int size();

	/**
	 * 清空
	 */
	public void clear();

	/**
	 * 遍历
	 */
	public E getitem();
}
