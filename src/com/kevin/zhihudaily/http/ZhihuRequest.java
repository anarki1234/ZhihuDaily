package com.kevin.zhihudaily.http;

import retrofit.RestAdapter;

public class ZhihuRequest {

    private static ZhihuRequestService mRequestService;

    public static final String BASE_URL = "http://news.at.zhihu.com/api/2";

    /**
     * ������Ϣ
     */
    public static final String GET_LATEST_NEWS = "/news/latest";

    /**
     * ��������ͼ���ȡ
     * ��start-image/�������Ϊͼ��ķֱ��ʣ�����������ֱ��ʣ����ֽ������¼���
     * ��ʽ��320*432��480*728��720*1184��1080*1776������ʽΪ��Щ֮��ģ���ȡ��ͼ��Ϊ�գ�
     */
    public static final String GET_START_IMAGE_XH = "/start-image/1080*1776";

    public static final String GET_START_IMAGE_H = "/start-image/720*1184";

    public static final String GET_START_IMAGE_M = "/start-image/480*728";

    public static final String GET_START_IMAGE_L = "/start-image/320*432";

    /**
     * ������Ϣ��
     * http://news.at.zhihu.com/api/2/news/before/20131119 
     * ˵����
     * a) ������Ҫ��ѯ11��18�յ���Ϣ��before/�������ӦΪ20131119
     * b) ֪���ձ�������Ϊ2013��5��19�գ���before/������С�ڴ˵�ֻ����ܵ�����Ϣ
     * c) ����Ľ���֮���������Ȼ��ý������ݣ����Ǹ�ʽ��ͬ��������Ϣ��JSON��ʽ
     */
    public static final String GET_OLD_NEWS = "/news/before/";

    /**
     * ������Ϣ
     */
    public static final String GET_HOT_NEWS = "/news/hot";

    /**
     * ����ƹ�
     */
    public static final String GET_PROMOTION = "/promotion/android?1386064554385 ";

    /**
     * ��Ŀ����
     *
     */
    public static final String GET_SECTIONS = "/sections";

    /**
     *  ��Ŀ������Ϣ�鿴��
     *  http://news-at.zhihu.com/api/2/section/1 ��URL�������ֲο�����Ŀ�������е�id���ԣ�
     */
    public static final String GET_SECTION_BY_TYPE = "/section/";

    /**
     *  ��Ŀ������Ϣ�鿴��
     *  http://news-at.zhihu.com/api/2/section/1 ��URL�������ֲο�����Ŀ�������е�id���ԣ�
     *  ��ǰ��http://news-at.zhihu.com/api/2/section/2/before/1384124400 
     * ������һ��ʱ�������һ��ʱ�����ʱ�����JSON������
     */
    public static final String GET_SECTION_BY_DATE = "http://news-at.zhihu.com/api/2/section/2/before/";

    //http://news-at.zhihu.com/api/3/story/3886491/long-comments
    public static final String GET_LONG_COMMENT = "api/3/story/3886491/long-comments";

    //http://news-at.zhihu.com/api/3/story/3886491/short-comments
    public static final String GET_SHORT_COMMENT = "api/3/story/3886491/short-comments";

    public static ZhihuRequestService getRequestService() {
        if (mRequestService == null) {
            RestAdapter adapter = new RestAdapter.Builder().setEndpoint(BASE_URL).build();

            mRequestService = adapter.create(ZhihuRequestService.class);
        }
        return mRequestService;
    }
}
