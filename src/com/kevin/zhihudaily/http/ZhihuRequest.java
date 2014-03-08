package com.kevin.zhihudaily.http;

public class ZhihuRequest {

    public static final String BASE_URL = "news-at.zhihu.com/api/2";

    /**
     * ������Ϣ
     */
    public static final String GET_LATEST = BASE_URL + "/news/lates";

    /**
     * ��������ͼ���ȡ
     * ��start-image/�������Ϊͼ��ķֱ��ʣ������������ֱ��ʣ����ֽ������¼���
     * ��ʽ��320*432��480*728��720*1184��1080*1776������ʽΪ��Щ֮��ģ���ȡ��ͼ��Ϊ�գ�
     */
    public static final String GET_START_IMAGE_XH = BASE_URL + "/start-image/1080*1776";

    public static final String GET_START_IMAGE_H = BASE_URL + "/start-image/720*1184";

    public static final String GET_START_IMAGE_M = BASE_URL + "/start-image/480*728";

    public static final String GET_START_IMAGE_L = BASE_URL + "/start-image/320*432";

    /**
     * ������Ϣ��
     * http://news.at.zhihu.com/api/2/news/before/20131119 
     * ˵����
     * a) ������Ҫ��ѯ11��18�յ���Ϣ��before/�������ӦΪ20131119
     * b) ֪���ձ�������Ϊ2013��5��19�գ���before/������С�ڴ˵�ֻ����ܵ�����Ϣ
     * c) ����Ľ���֮���������Ȼ��ý������ݣ����Ǹ�ʽ��ͬ��������Ϣ��JSON��ʽ
     */
    public static final String GET_OLD_NEWS = "http://news.at.zhihu.com/api/2/news/before/";

    /**
     * ������Ϣ
     */
    public static final String GET_HOT_NEWS = BASE_URL + "/news/hot";

    /**
     * ����ƹ�
     */
    public static final String GET_PROMOTION = BASE_URL + "/promotion/android?1386064554385 ";

    /**
     * ��Ŀ����
     *
     */
    public static final String GET_SECTIONS = BASE_URL + "/sections";

    /**
     *  ��Ŀ������Ϣ�鿴��
     *  http://news-at.zhihu.com/api/2/section/1 ��URL�������ֲο�����Ŀ�������е�id���ԣ�
     */
    public static final String GET_SECTION_BY_TYPE = BASE_URL + "/section/%d ";

    /**
     *  ��Ŀ������Ϣ�鿴��
     *  http://news-at.zhihu.com/api/2/section/1 ��URL�������ֲο�����Ŀ�������е�id���ԣ�
     *  ��ǰ��http://news-at.zhihu.com/api/2/section/2/before/1384124400 
     * ������һ��ʱ�������һ��ʱ�����ʱ�����JSON�������
     */
    public static final String GET_SECTION_BY_DATE = "http://news-at.zhihu.com/api/2/section/2/before/%l";
}
