/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  ��ʾ����λ�ȡ��ȫУ����ͺ��������id
 *  1.������ǩԼ֧�����˺ŵ�¼֧������վ(www.alipay.com)
 *  2.������̼ҷ���(https://b.alipay.com/order/myorder.htm)
 *  3.�������ѯ���������(pid)��������ѯ��ȫУ����(key)��
 */

package com.alipay.sdk.pay.demo;

//
// ��ο� Androidƽ̨��ȫ֧������(msp)Ӧ�ÿ����ӿ�(4.2 RSA�㷨ǩ��)���֣���ʹ��ѹ�����е�openssl RSA��Կ���ɹ��ߣ�����һ��RSA��˽Կ��
// ����ǩ��ʱ��ֻ��Ҫʹ�����ɵ�RSA˽Կ��
// Note: Ϊ��ȫ�����ʹ��RSA˽Կ����ǩ���Ĳ������̣�Ӧ�þ����ŵ��̼ҷ�������ȥ���С�
public final class Keys {

	// ���������id����2088��ͷ��16λ������
	public static final String DEFAULT_PARTNER = "2088512155291258";

	// �տ�֧�����˺�
	public static final String DEFAULT_SELLER = "yaodi4329@163.com";

	// �̻�˽Կ����������
	public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL414Yvh7tcr7T4WRjrBW1zJzXYBeTERf+eoe4b9Z0Co7DNOqs8WtxOS7fXlomN3onmxHDeAUCKZe2OPK86aQCmq/59doE4kiojn4r8aptK/i+uz+MNbWkkDoudq67pEFJ+/jmtOaZ9nYc9t9qdK+CfHFv/6yEKZ7bLswom49kILAgMBAAECgYAaLdhJJGN3Afi5Ht+znoEwmCXLJBDGDB6Kh8nYnm/jmlxIA3/G2N2BuCtAOMbM4d5e2V2E1ggxeuZcoYvp95j05l84JIbNYkRnqD9kNmAhg453P95dx1RPM5W0V2ACgABeMpbHhdJiAS+V9Qly/4GTIiwRnTVmluFFgyQzBXpiQQJBAOfInz5wWVX+Xji6RfIBhcI9ydgBme18cZK+GF3r3UxtHt7v45oSNMxtQ7VLC9erHSygCdHOuhijNU0drXEtRuMCQQDSFVNX3dK4BMorRLpiwtIM29iRrIdhUcr0yOI0uTJzqAI2arPZS/SND1y0QntLqg6yjgNoAmTZniXbT1B7oFi5AkAOnuMcpy2bdQ4wed+Lonjzhb5Dt+YjyuPHI+KZmhU9iuyVl4A96cQW2RWaS/+VPR/7/qRf7bBjeXgVQzf7maWNAkEAkaN5EKvV3f5sDQzrTXYT2hg6jdqWS7BBS+tLbhOJV0Z8qi7UmWqfkX653LlN8kllQQFUeTFXqsmJIv1cJSgm+QJAKuR0NEaL/UHEQO/BgFUOULXNW5b43WAh1b5DaCuSekofkKG55ipZ1Gv6hYYvORxrLJwA61cly4EHuh5eOrI6qA==";

	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
}