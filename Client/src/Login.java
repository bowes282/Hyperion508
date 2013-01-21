/* Login - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
import java.io.*;
import java.net.Socket;

public class Login
{
    public static int anInt1497;
    public static Login aLogin_1498 = new Login(16);
    public static int anInt1499;
    public static int anInt1500;
    public static int anInt1501;
    public static int anInt1502;
    public static int anInt1503;
    public static int anInt1504;
    public static int anInt1505;
    public Class68_Sub20 aClass68_Sub20_1506 = new Class68_Sub20();
    public static RSString aRSString_1507;
    public static int anInt1508;
    public static int anInt1509;
    public static int[] anIntArray1510 = new int[2];
    public static RSString aRSString_1511 = Class112.method1668(43, "World");
    public static Class113 aClass113_1512;
    public static RSString aRSString_1513;
    public static int anInt1514;
    public static int[] anIntArray1515;
    public int anInt1516;
    public int anInt1517;
    public static int anInt1518;
    public Class113 aClass113_1519;
    public Class128 aClass128_1520 = new Class128();
    public static RSString aRSString_1521;
    public static RSString aRSString_1522;
    
    public static void method1463(boolean bool) {
	anInt1500++;
	if (Class32.anInt574 != 0 && Class32.anInt574 != 5) {
	    try {
		if (++Class68_Sub13_Sub24.anInt3841 > 2000) {
		    if (Class126.aClass61_2190 != null) {
			Class126.aClass61_2190.method594(-64);
			Class126.aClass61_2190 = null;
		    }
		    if (Class38.anInt666 >= 1) {
			Class68_Sub20_Sub7.anInt4257 = -5;
			Class32.anInt574 = 0;
			return;
		    }
		    if (Class115.anInt1946 != Class103.anInt1773)
			Class103.anInt1773 = Class115.anInt1946;
		    else
			Class103.anInt1773 = Class68_Sub15.anInt3013;
		    Class38.anInt666++;
		    Class32.anInt574 = 1;
		    Class68_Sub13_Sub24.anInt3841 = 0;
		}
		if (bool != false)
		    aRSString_1522 = null;
		if (Class32.anInt574 == 1) {
		    Class13_Sub2.aClass31_2657 = (Class75_Sub1_Sub1.aSignLink_4589.method559(Class68_Sub9.aString2883, (byte) 116, Class103.anInt1773));
		    Class32.anInt574 = 2;
		}
		if (Class32.anInt574 == 2) {
		    if (Class13_Sub2.aClass31_2657.anInt555 == 2)
			throw new IOException();
		    if (Class13_Sub2.aClass31_2657.anInt555 != 1)
			return;
		    Class126.aClass61_2190 = new Class61(((Socket) Class13_Sub2.aClass31_2657.anObject558), Class75_Sub1_Sub1.aSignLink_4589);
		    Class13_Sub2.aClass31_2657 = null;
		    long l = (Class72_Sub1.aLong3285 = Class68_Sub28_Sub2.aRSString_4551.method1609(10908));
		    Class21.aPacketStream_466.anInt2945 = 0;
		    Class21.aPacketStream_466.writeByte(32, 14);
		    int i = (int) (0x1fL & l >> 502092432);
		    Class21.aPacketStream_466.writeByte(32, i);
		    Class126.aClass61_2190.method590(0, (byte) 36, 2, (Class21.aPacketStream_466.aByteArray2960));
		    if (Class68_Sub13_Sub36.aClass35_4035 != null)
			Class68_Sub13_Sub36.aClass35_4035.method418(bool);
		    if (Class1.aClass35_66 != null)
			Class1.aClass35_66.method418(bool);
		    int i_0_ = Class126.aClass61_2190.method593(112);
		    if (Class68_Sub13_Sub36.aClass35_4035 != null)
			Class68_Sub13_Sub36.aClass35_4035.method418(false);
		    if (Class1.aClass35_66 != null)
			Class1.aClass35_66.method418(false);
		    if (i_0_ != 0) {
			Class68_Sub20_Sub7.anInt4257 = i_0_;
			Class32.anInt574 = 0;
			Class126.aClass61_2190.method594(-7);
			Class126.aClass61_2190 = null;
			return;
		    }
		    Class32.anInt574 = 3;
		}
		if (Class32.anInt574 == 3) {
		    if (Class126.aClass61_2190.method597((byte) 102) < 8)
			return;
		    Class126.aClass61_2190.method591(-44, 8, 0, (Class68_Sub13_Sub8.aPacketStream_3560.aByteArray2960));
		    Class68_Sub13_Sub8.aPacketStream_3560.anInt2945 = 0;
		    Class68_Sub13_Sub11.aLong3603 = Class68_Sub13_Sub8.aPacketStream_3560.method941(102);
		    int[] is = new int[4];
		    is[1] = (int) (9.9999999E7 * Math.random());
		    is[0] = (int) (9.9999999E7 * Math.random());
		    is[2] = (int) (Class68_Sub13_Sub11.aLong3603 >> 266129824);
		    Class21.aPacketStream_466.anInt2945 = 0;
		    is[3] = (int) Class68_Sub13_Sub11.aLong3603;
		    Class21.aPacketStream_466.writeByte(32, 10);
		    Class21.aPacketStream_466.writeDWord(544537784, is[0]);
		    Class21.aPacketStream_466.writeDWord(544537784, is[1]);
		    Class21.aPacketStream_466.writeDWord(544537784, is[2]);
		    Class21.aPacketStream_466.writeDWord(544537784, is[3]);
		    Class21.aPacketStream_466.method924(true, Class68_Sub28_Sub2.aRSString_4551.method1609(10908));
		    Class21.aPacketStream_466.method914((byte) -9, Class68_Sub28_Sub2.aRSString_4552);
		    Class21.aPacketStream_466.method933(false, Class68_Sub4.aBigInteger2814, Class68_Sub22.aBigInteger3136);
		    Class112.aPacketStream_1898.anInt2945 = 0;
		    if (RuntimeException_Sub1.anInt2231 == 40)
			Class112.aPacketStream_1898.writeByte(32, 18);
		    else
			Class112.aPacketStream_1898.writeByte(32, 16);
		    Class112.aPacketStream_1898.writeWord(false, (151 + Class21.aPacketStream_466.anInt2945 + Class13_Sub1.method250(Class26.aRSString_521, -106)));
		    Class112.aPacketStream_1898.writeDWord(544537784, 508);
		    Class112.aPacketStream_1898.writeByte(32, Class68_Sub13_Sub5.anInt3518);
		    Class112.aPacketStream_1898.writeByte(32, 0);
		    Class112.aPacketStream_1898.writeByte(32, Class71_Sub1.method1344((byte) 97));
		    Class112.aPacketStream_1898.writeWord(bool, Class13_Sub3.anInt2668);
		    Class112.aPacketStream_1898.writeWord(false, Class68_Sub13_Sub9.anInt3568);
		    Class20.method322(24, Class112.aPacketStream_1898);
		    Class112.aPacketStream_1898.method914((byte) -9, Class26.aRSString_521);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68_Sub13_Sub20.anInt3780);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class34.method415((byte) 119));
		    Class108.aBoolean1830 = true;
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68_Sub28_Sub1.aClass21_Sub1_4519.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class58.aClass21_Sub1_1097.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68_Sub6.aClass21_Sub1_2843.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68_Sub13_Sub21.aClass21_Sub1_3804.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class78.aClass21_Sub1_1388.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class92.aClass21_Sub1_1644.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class21.aClass21_Sub1_459.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class120.aClass21_Sub1_2095.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class52.aClass21_Sub1_1011.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68_Sub13_Sub30.aClass21_Sub1_3953.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class84.aClass21_Sub1_1532.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class21.aClass21_Sub1_472.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class13.aClass21_Sub1_221.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class63.aClass21_Sub1_1173.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68_Sub20_Sub4.aClass21_Sub1_4217.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class28.aClass21_Sub1_529.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class7.aClass21_Sub1_136.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class33.aClass21_Sub1_583.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68.aClass21_Sub1_1228.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class55.aClass21_Sub1_2038.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class57.aClass21_Sub1_1068.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68_Sub13_Sub31.aClass21_Sub1_3971.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class14.aClass21_Sub1_229.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68_Sub20_Sub5.aClass21_Sub1_4221.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class68_Sub13_Sub5.aClass21_Sub1_3522.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, Class38.aClass21_Sub1_659.anInt428);
		    Class112.aPacketStream_1898.writeDWord(544537784, PacketStream.aClass21_Sub1_4133.anInt428);
		    Class112.aPacketStream_1898.method942(Class21.aPacketStream_466.anInt2945, (byte) 118, 0, Class21.aPacketStream_466.aByteArray2960);
		    Class126.aClass61_2190.method590(0, (byte) 86, Class112.aPacketStream_1898.anInt2945, Class112.aPacketStream_1898.aByteArray2960);
		    Class21.aPacketStream_466.method964(is, 3426);
		    for (int i = 0; i < 4; i++)
			is[i] += 50;
		    Class68_Sub13_Sub8.aPacketStream_3560.method964(is, 3426);
		    Class32.anInt574 = 4;
		}
		if (Class32.anInt574 == 4) {
		    if (Class126.aClass61_2190.method597((byte) 102) < 1)
			return;
		    int i = Class126.aClass61_2190.method593(110);
		    if (i == 21)
			Class32.anInt574 = 7;
		    else if (i != 1) {
			if (i == 2)
			    Class32.anInt574 = 8;
			else {
			    if (i != 15) {
				if (i != 23 || Class38.anInt666 >= 1) {
				    Class68_Sub20_Sub7.anInt4257 = i;
				    Class32.anInt574 = 0;
				    Class126.aClass61_2190.method594(-41);
				    Class126.aClass61_2190 = null;
				} else {
				    Class32.anInt574 = 1;
				    Class68_Sub13_Sub24.anInt3841 = 0;
				    Class38.anInt666++;
				    Class126.aClass61_2190.method594(-48);
				    Class126.aClass61_2190 = null;
				}
			    } else {
				Class68_Sub20_Sub7.anInt4257 = i;
				Class32.anInt574 = 0;
				return;
			    }
			    return;
			}
		    } else {
			Class32.anInt574 = 5;
			Class68_Sub20_Sub7.anInt4257 = i;
			return;
		    }
		}
		if (Class32.anInt574 == 6) {
		    Class21.aPacketStream_466.anInt2945 = 0;
		    Class21.aPacketStream_466.createFrame(115, 17);
		    Class126.aClass61_2190.method590(0, (byte) 87, Class21.aPacketStream_466.anInt2945, Class21.aPacketStream_466.aByteArray2960);
		    Class32.anInt574 = 4;
		} else if (Class32.anInt574 == 7) {
		    if (Class126.aClass61_2190.method597((byte) 102) >= 1) {
			Class36.anInt642 = 180 + Class126.aClass61_2190.method593(98) * 60;
			Class32.anInt574 = 0;
			Class68_Sub20_Sub7.anInt4257 = 21;
			Class126.aClass61_2190.method594(-51);
			Class126.aClass61_2190 = null;
		    }
		} else {
		    if (Class32.anInt574 == 8) {
			if (Class126.aClass61_2190.method597((byte) 102) < 11)
			    return;
			Class126.aClass61_2190.method591(-73, 11, 0, (Class68_Sub13_Sub8.aPacketStream_3560.aByteArray2960));
			Class68_Sub13_Sub8.aPacketStream_3560.anInt2945 = 0;
			Class68_Sub13_Sub13.anInt3663 = Class68_Sub13_Sub8.aPacketStream_3560.readUnsignedByte(-6677);
			Class8.anInt160 = Class68_Sub13_Sub8.aPacketStream_3560.readUnsignedByte(-6677);
			Class25.anInt517 = Class68_Sub13_Sub8.aPacketStream_3560.readUnsignedByte(-6677);
			if (Class25.anInt517 != 1) {
			    try {
				Class68_Sub20_Sub8.aRSString_4282.method1604((Class75_Sub1_Sub1.aSignLink_4589.anApplet1042), (byte) -123);
			    } catch (Throwable throwable) {
				/* empty */
			    }
			} else {
			    try {
				Class68_Sub4.aRSString_2829.method1604((Class75_Sub1_Sub1.aSignLink_4589.anApplet1042), (byte) -123);
			    } catch (Throwable throwable) {
				/* empty */
			    }
			}
			Class30.anInt552 = Class68_Sub13_Sub8.aPacketStream_3560.readUnsignedByte(-6677);
			Class68_Sub13_Sub33.aBoolean4000 = Class68_Sub13_Sub8.aPacketStream_3560.readUnsignedByte(-6677) == 1;
			Class68_Sub10.anInt2911 = Class68_Sub13_Sub8.aPacketStream_3560.readUnsignedWord(1355769544);
			Class26.anInt519 = Class68_Sub13_Sub8.aPacketStream_3560.readUnsignedByte(-6677);
			Class49.anInt962 = Class68_Sub13_Sub8.aPacketStream_3560.method972(255);
			Class106.anInt1804 = Class68_Sub13_Sub8.aPacketStream_3560.readUnsignedWord(1355769544);
			Class32.anInt574 = 9;
		    }
		    if (Class32.anInt574 == 9) {
			if (Class126.aClass61_2190.method597((byte) 102) >= Class106.anInt1804) {
			    Class68_Sub13_Sub8.aPacketStream_3560.anInt2945 = 0;
			    Class126.aClass61_2190.method591(88, Class106.anInt1804, 0, (Class68_Sub13_Sub8.aPacketStream_3560.aByteArray2960));
			    Class32.anInt574 = 0;
			    Class68_Sub20_Sub7.anInt4257 = 2;
			    Class21.method358(10938);
			    ItemDef.anInt365 = -1;
			    Class68_Sub13_Sub15.method768((byte) 95, false);
			    Class49.anInt962 = -1;
			}
		    }
		}
	    } catch (IOException ioexception) {
		if (Class126.aClass61_2190 != null) {
		    Class126.aClass61_2190.method594(-94);
		    Class126.aClass61_2190 = null;
		}
		if (Class38.anInt666 < 1) {
		    Class32.anInt574 = 1;
		    Class68_Sub13_Sub24.anInt3841 = 0;
		    Class38.anInt666++;
		    if (Class103.anInt1773 != Class115.anInt1946)
			Class103.anInt1773 = Class115.anInt1946;
		    else
			Class103.anInt1773 = Class68_Sub15.anInt3013;
		} else {
		    Class32.anInt574 = 0;
		    Class68_Sub20_Sub7.anInt4257 = -4;
		}
	    }
	}
    }
    
    public static void method1464(int i, int i_1_) {
	anInt1502++;
	Class68_Sub13_Sub8.aClass98_3561.method1564(-18767, i_1_);
	int i_2_ = -47 % ((i - 38) / 47);
    }
    
    public void method1465(long l, byte i, Class68_Sub20 class68_sub20) {
	try {
	    if (anInt1516 != 0)
		anInt1516--;
	    else {
		Class68_Sub20 class68_sub20_3_
		    = aClass128_1520.method1790(2265);
		class68_sub20_3_.method647(-17554);
		class68_sub20_3_.method1005((byte) 73);
		if (aClass68_Sub20_1506 == class68_sub20_3_) {
		    class68_sub20_3_ = aClass128_1520.method1790(2265);
		    class68_sub20_3_.method647(-17554);
		    class68_sub20_3_.method1005((byte) 73);
		}
	    }
	    anInt1505++;
	    aClass113_1519.method1677((byte) 9, class68_sub20, l);
	    int i_4_ = -84 % ((43 - i) / 39);
	    aClass128_1520.method1787(48, class68_sub20);
	} catch (RuntimeException runtimeexception) {
	    throw Class107.method1652(runtimeexception,
				      ("nj.E(" + l + ',' + i + ','
				       + (class68_sub20 != null ? "{...}"
					  : "null")
				       + ')'));
	}
    }
    
    public Class68 method1466(int i) {
	anInt1499++;
	if (i <= 37)
	    return null;
	return aClass113_1519.method1683((byte) -122);
    }
    
    public static void method1467(byte i) {
	aRSString_1522 = null;
	aRSString_1513 = null;
	aRSString_1521 = null;
	if (i != 69)
	    method1463(true);
	aClass113_1512 = null;
	anIntArray1510 = null;
	aRSString_1507 = null;
	anIntArray1515 = null;
	aRSString_1511 = null;
	aLogin_1498 = null;
    }
    
    public static boolean method1468(Class68_Sub28_Sub1 class68_sub28_sub1,
				     Class21 class21, boolean bool,
				     Class21 class21_5_, Class21 class21_6_) {
	try {
	    Class83.aClass21_1529 = class21_6_;
	    Class125.aClass21_2170 = class21_5_;
	    if (bool != false)
		return true;
	    Class1_Sub2.aClass68_Sub28_Sub1_2433 = class68_sub28_sub1;
	    anInt1501++;
	    Class68_Sub20_Sub8.aClass21_4279 = class21;
	    return true;
	} catch (RuntimeException runtimeexception) {
	    throw Class107.method1652
		      (runtimeexception,
		       ("nj.K("
			+ (class68_sub28_sub1 != null ? "{...}" : "null") + ','
			+ (class21 != null ? "{...}" : "null") + ',' + bool
			+ ',' + (class21_5_ != null ? "{...}" : "null") + ','
			+ (class21_6_ != null ? "{...}" : "null") + ')'));
	}
    }
    
    public static void method1469(int i) {
	if (i < 115)
	    method1467((byte) -105);
	anInt1504++;
	Class68_Sub13_Sub8.aClass113_3564 = new Class113(32);
    }
    
    public void method1470(byte i) {
	anInt1497++;
	aClass128_1520.method1783((byte) -56);
	aClass113_1519.method1684(false);
	aClass68_Sub20_1506 = new Class68_Sub20();
	if (i != -11)
	    aRSString_1511 = null;
	anInt1516 = anInt1517;
    }
    
    public static void method1471(int i, int i_7_) {
	int i_8_ = 97 / ((63 - i_7_) / 48);
	anInt1508++;
	PacketStream.aClass98_4118.method1564(-18767, i);
    }
    
    public static boolean method1472(int i, int i_9_, int i_10_) {
	for (int i_11_ = 0; i_11_ < Class1_Sub7.anInt2597; i_11_++) {
	    Class8 class8 = Class68_Sub25.aClass8Array3168[i_11_];
	    if (class8.anInt141 == 1) {
		int i_12_ = class8.anInt159 - i;
		if (i_12_ > 0) {
		    int i_13_
			= class8.anInt148 + (class8.anInt138 * i_12_ >> 8);
		    int i_14_
			= class8.anInt147 + (class8.anInt157 * i_12_ >> 8);
		    int i_15_
			= class8.anInt152 + (class8.anInt153 * i_12_ >> 8);
		    int i_16_
			= class8.anInt150 + (class8.anInt143 * i_12_ >> 8);
		    if (i_10_ >= i_13_ && i_10_ <= i_14_ && i_9_ >= i_15_
			&& i_9_ <= i_16_)
			return true;
		}
	    } else if (class8.anInt141 == 2) {
		int i_17_ = i - class8.anInt159;
		if (i_17_ > 0) {
		    int i_18_
			= class8.anInt148 + (class8.anInt138 * i_17_ >> 8);
		    int i_19_
			= class8.anInt147 + (class8.anInt157 * i_17_ >> 8);
		    int i_20_
			= class8.anInt152 + (class8.anInt153 * i_17_ >> 8);
		    int i_21_
			= class8.anInt150 + (class8.anInt143 * i_17_ >> 8);
		    if (i_10_ >= i_18_ && i_10_ <= i_19_ && i_9_ >= i_20_
			&& i_9_ <= i_21_)
			return true;
		}
	    } else if (class8.anInt141 == 3) {
		int i_22_ = class8.anInt148 - i_10_;
		if (i_22_ > 0) {
		    int i_23_
			= class8.anInt159 + (class8.anInt139 * i_22_ >> 8);
		    int i_24_
			= class8.anInt162 + (class8.anInt144 * i_22_ >> 8);
		    int i_25_
			= class8.anInt152 + (class8.anInt153 * i_22_ >> 8);
		    int i_26_
			= class8.anInt150 + (class8.anInt143 * i_22_ >> 8);
		    if (i >= i_23_ && i <= i_24_ && i_9_ >= i_25_
			&& i_9_ <= i_26_)
			return true;
		}
	    } else if (class8.anInt141 == 4) {
		int i_27_ = i_10_ - class8.anInt148;
		if (i_27_ > 0) {
		    int i_28_
			= class8.anInt159 + (class8.anInt139 * i_27_ >> 8);
		    int i_29_
			= class8.anInt162 + (class8.anInt144 * i_27_ >> 8);
		    int i_30_
			= class8.anInt152 + (class8.anInt153 * i_27_ >> 8);
		    int i_31_
			= class8.anInt150 + (class8.anInt143 * i_27_ >> 8);
		    if (i >= i_28_ && i <= i_29_ && i_9_ >= i_30_
			&& i_9_ <= i_31_)
			return true;
		}
	    } else if (class8.anInt141 == 5) {
		int i_32_ = i_9_ - class8.anInt152;
		if (i_32_ > 0) {
		    int i_33_
			= class8.anInt159 + (class8.anInt139 * i_32_ >> 8);
		    int i_34_
			= class8.anInt162 + (class8.anInt144 * i_32_ >> 8);
		    int i_35_
			= class8.anInt148 + (class8.anInt138 * i_32_ >> 8);
		    int i_36_
			= class8.anInt147 + (class8.anInt157 * i_32_ >> 8);
		    if (i >= i_33_ && i <= i_34_ && i_10_ >= i_35_
			&& i_10_ <= i_36_)
			return true;
		}
	    }
	}
	return false;
    }
    
    public Class68_Sub20 method1473(long l, byte i) {
	try {
	    if (i != -76)
		aClass68_Sub20_1506 = null;
	    Class68_Sub20 class68_sub20
		= (Class68_Sub20) aClass113_1519.method1678(l, -111);
	    anInt1503++;
	    if (class68_sub20 != null)
		aClass128_1520.method1787(77, class68_sub20);
	    return class68_sub20;
	} catch (RuntimeException runtimeexception) {
	    throw Class107.method1652(runtimeexception,
				      "nj.I(" + l + ',' + i + ')');
	}
    }
    
    public Class68 method1474(int i) {
	anInt1518++;
	if (i != 0)
	    aRSString_1507 = null;
	return aClass113_1519.method1679(-32642);
    }
    
    public Login(int i) {
	anInt1516 = i;
	anInt1517 = i;
	int i_37_;
	for (i_37_ = 1; (i_37_ + i_37_ ^ 0xffffffff) > (i ^ 0xffffffff);
	     i_37_ += i_37_) {
	    /* empty */
	}
	aClass113_1519 = new Class113(i_37_);
    }
    
    static {
	aRSString_1507 = aRSString_1511;
	aRSString_1513 = Class112.method1668(43, "Hidden)2");
	anIntArray1515 = new int[50];
	aClass113_1512 = new Class113(8);
	aRSString_1522 = Class112.method1668(43, "Loaded config");
	aRSString_1521 = aRSString_1522;
    }
}
