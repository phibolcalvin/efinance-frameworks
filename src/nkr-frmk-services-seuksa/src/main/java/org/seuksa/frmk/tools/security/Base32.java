package org.seuksa.frmk.tools.security;

/* Base32
2 *
3 * $Id: Base32.java,v 1.4 2004/04/15 19:04:01 stack-sf Exp $
4 *
5 * Created on Jan 21, 2004
6 *
7 * Copyright (C) 2004 Internet Archive.
8 *
9 * This file is part of the Heritrix web crawler (crawler.archive.org).
10 *
11 * Heritrix is free software; you can redistribute it and/or modify
12 * it under the terms of the GNU Lesser Public License as published by
13 * the Free Software Foundation; either version 2.1 of the License, or
14 * any later version.
15 *
16 * Heritrix is distributed in the hope that it will be useful,
17 * but WITHOUT ANY WARRANTY; without even the implied warranty of
18 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
19 * GNU Lesser Public License for more details.
20 *
21 * You should have received a copy of the GNU Lesser Public License
22 * along with Heritrix; if not, write to the Free Software
23 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
24 */
/**
* Base32 - encodes and decodes RFC3548 Base32
* (see http://www.faqs.org/rfcs/rfc3548.html )
*
* Imported public-domain code of Bitzi.
*
* @author Robert Kaye
* @author Gordon Mohr
*/
 public class Base32 
{
     private static final String   	base32Chars 	=   "23456789ABCDEFH1JKLMN0PQRSTUWXYZ";
 
     private static final int[] 	base32Lookup 	=
     { 
    	 0x15,0x0F,0x00,0x01,0x02,0x03,0x04,0x05, // '0', '1', '2', '3', '4', '5', '6', '7'
    	 0x06,0x07,0xFF,0xFF,0xFF,0xFF,0xFF,0xFF, // '8', '9', ':', ';', '<', '=', '>', '?'
    	 0xFF,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0xFF, // '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G'
    	 0x0E,0xFF,0x10,0x11,0x12,0x13,0x14,0xFF, // 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O'
    	 0x16,0x17,0x18,0x19,0x1A,0x1B,0xFF,0x1C, // 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W'
    	 0x1D,0x1E,0x1F,0xFF,0xFF,0xFF,0xFF,0xFF, // 'X', 'Y', 'Z', '[', '\', ']', '^', '_'
    	 0xFF,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0xFF, // '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g'
    	 0x0E,0xFF,0x10,0x11,0x12,0x13,0x14,0xFF, // 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o'
    	 0x16,0x17,0x18,0x19,0x1A,0x1B,0xFF,0x1C, // 'p', 'q', 'r', 's', 't', 'u', 'v', 'w'
    	 0x1D,0x1E,0x1F,0xFF,0xFF,0xFF,0xFF,0xFF  // 'x', 'y', 'z', '{', '|', '}', '~', 'DEL'
     };
     
     /**
      * Encodes byte array to Base32 String.
      *
      * @param bytes Bytes to encode.
      * @return Encoded byte array <code>bytes</code> as a String.
      *
      */
     public static String encode(final byte[] bytes) 
     {
         int i = 0, index = 0, digit = 0;
         int currByte, nextByte;
         StringBuffer   base32 = new StringBuffer  ((bytes.length + 7) * 8 / 5);
 
         while (i < bytes.length) {
             currByte = (bytes[i] >= 0) ? bytes[i] : (bytes[i] + 256); // unsign
 
             /* Is the current digit going to span a byte boundary? */
             if (index > 3) {
                 if ((i + 1) < bytes.length) {
                     nextByte =
                         (bytes[i + 1] >= 0) ? bytes[i + 1] : (bytes[i + 1] + 256);
                 } else {
                     nextByte = 0;
                 }
 
                 digit = currByte & (0xFF >> index);
                 index = (index + 5) % 8;
                 digit <<= index;
                 digit |= nextByte >> (8 - index);
                 i++;
             } else {
                 digit = (currByte >> (8 - (index + 5))) & 0x1F;
                 index = (index + 5) % 8;
                 if (index == 0)
                     i++;
             }
             base32.append(base32Chars.charAt(digit));
         }
 
         return base32.toString();
     }
 
     public static byte[] decode(final String base32, int length)
     {
    	 byte[] res = decode(base32);
    	 
    	if (res.length > length)
     	{
     		byte[] temp = new byte[length];
     	   	System.arraycopy(res, 0, temp, 0, length);
     	   	return temp;
     	} 
    	 
    	 return res;
     }
     
     /**
      * Decodes the given Base32 String to a raw byte array.
      *
      * @param base32
      * @return Decoded <code>base32</code> String as a raw byte array.
      */
     public static byte[] decode(final String   base32) {
         int i, index, lookup, offset, digit;
         
         int length = (base32.length() * 5)/8;
         
         if ((length * 8) != (base32.length() * 5))
        	length++;
 
         byte[] bytes = new byte[length];
         
         for (i = 0, index = 0, offset = 0; i < base32.length(); i++) {
             lookup = base32.charAt(i) - '0';
 
             /* Skip chars outside the lookup table */
             if (lookup < 0 || lookup >= base32Lookup.length) {
                 continue;
             }
 
             digit = base32Lookup[lookup];
 
             /* If this digit is not in the table, ignore it */
             if (digit == 0xFF) {
                 continue;
             }
 
             if (index <= 3) {
                 index = (index + 5) % 8;
                 if (index == 0) {
                     bytes[offset] |= digit;
                     offset++;
                     if (offset >= bytes.length)
                         break;
                 } else {
                     bytes[offset] |= digit << (8 - index);
                 }
             } else {
                 index = (index + 5) % 8;
                 bytes[offset] |= (digit >>> index);
                 offset++;
 
                 if (offset >= bytes.length) {
                     break;
                 }
                 bytes[offset] |= digit << (8 - index);
             }
         }
         return bytes;
     }
 
     /** For testing, take a command-line argument in Base32, decode, print in hex,
      * encode, print
      *
      * @param args
      */
     public static void main(String  [] args) {
         /*
    	 if (args.length == 0) 
         {
             System.out.println("Supply a Base32-encoded argument.");
             return;
         }
    	 */
    	 
    	 String arg = "ABCDEFGHJKLMNPQRSTUVWXYZ2345";
    	 System.out.println(" Original["+arg.length()+"]: " + arg);
         byte[] decoded = Base32.decode(arg);
         System.out.print(" Hex: ");
         for (int i = 0; i < decoded.length; i++) {
             int b = decoded[i];
             if (b < 0) {
                 b += 256;
             }
             System.out.print((Integer.toHexString(b + 256)).substring(1));
         }
         System.out.println();
         System.out.println("Reencoded: " + Base32.encode(decoded));
         
         byte[] data = {0x00,0x44,0x32,0x14};
         String s = Base32.encode(data);
         System.out.println("encoded: " + s);
         decoded = Base32.decode(s);
         System.out.print(" Hex: ");
         for (int i = 0; i < decoded.length; i++) {
             int b = decoded[i];
             if (b < 0) {
                 b += 256;
             }
             System.out.print((Integer.toHexString(b + 256)).substring(1));
         }
         System.out.println();
         
         System.out.println("_______");
         
         String sBase = toBase((int) 4469268,10,0);
         System.out.println("sBase = "+sBase);
         fromBase(sBase,10);
         
     }
     
    public static String toBase(int num, int base, int size)
 	{
 		int x = num;
 		int y = base;
 		int i = 0;
 		int[] trans = new int[100];
 		String result ="";


 		do
 		{
 			trans[i] = x % y;
 			x = x / y;
 			i++;
 		}while(x != 0);

 		// display
 		for(int k = i; k >= 0; k--)
 		{
 			if(trans[k] == 10)
 			{
 				result += "A";
 			}
 			else if(trans[k] == 11)
 			{
 				result += "B";
 			}
 			else if(trans[k] == 12)
 			{
 				result += "C";
 			}
 			else if(trans[k] == 13)
 			{
 				result += "D";
 			}
 			else if(trans[k] == 14)
 			{
 				result += "E";
 			}
 			else if(trans[k] == 15)
 			{
 				result += "F";
 			}
 			else if(trans[k] == 16)
 			{
 				result += "G";
 			}
 			else if(trans[k] == 17)
 			{
 				result += "H";
 			}
 			else if(trans[k] == 18)
 			{
 				result += "I";
 			}
 			else if(trans[k] == 19)
 			{
 				result += "J";
 			}
 			else if(trans[k] == 20)
 			{
 				result += "K";
 			}
 			else if(trans[k] == 21)
 			{
 				result += "L";
 			}
 			else if(trans[k] == 22)
 			{
 				result += "M";
 			}
 			else if(trans[k] == 23)
 			{
 				result += "N";
 			}
 			else if(trans[k] == 24)
 			{
 				result += "O";
 			}
 			else if(trans[k] == 25)
 			{
 				result += "P";
 			}
 			else if(trans[k] == 26)
 			{
 				result += "Q";
 			}
 			else if(trans[k] == 27)
 			{
 				result += "R";
 			}
 			else if(trans[k] == 28)
 			{
 				result += "S";
 			}
 			else if(trans[k] == 29)
 			{
 				result += "T";
 			}
 			else if(trans[k] == 30)
 			{
 				result += "U";
 			}
 			else if(trans[k] == 31)
 			{
 				result += "V";
 			}
 			else if(trans[k] == 32)
 			{
 				result += "W";
 			}
 			else if(trans[k] == 33)
 			{
 				result += "X";
 			}
 			else if(trans[k] == 34)
 			{
 				result += "Y";
 			}
 			else if(trans[k] == 35)
 			{
 				result += "Z";
 			}
 			else
 			{
 				result += String.valueOf(trans[k]);
 			}
 		}
 		
 		return result;
 	}
    
    public static int fromBase(String input,int base)
    {
     
			// variables						
			int temp1;
			int ans;
			int size = input.length();

			// set number into array and convert letters to numbers
			int[] num = new int[size];
			for(int i = 0; i < size; i++)
			{
				if(input.charAt(i) == 'A' || input.charAt(i) == 'a')
				{
					num[i] = 10;
				}
				else if(input.charAt(i) == 'B' || input.charAt(i) == 'b')
				{
					num[i] = 11;
				}
				else if(input.charAt(i) == 'C' || input.charAt(i) == 'c')
				{
					num[i] = 12;
				}
				else if(input.charAt(i) == 'D' || input.charAt(i) == 'd')
				{
					num[i] = 13;
				}
				else if(input.charAt(i) == 'E' || input.charAt(i) == 'e')
				{
					num[i] = 14;
				}
				else if(input.charAt(i) == 'F' || input.charAt(i) == 'f')
				{
					num[i] = 15;
				}
				else if(input.charAt(i) == 'G' || input.charAt(i) == 'g')
				{
					num[i] = 16;
				}
				else if(input.charAt(i) == 'H' || input.charAt(i) == 'h')
				{
					num[i] = 17;
				}
				else if(input.charAt(i) == 'I' || input.charAt(i) == 'i')
				{
					num[i] = 18;
				}
				else if(input.charAt(i) == 'J' || input.charAt(i) == 'j')
				{
					num[i] = 19;
				}
				else if(input.charAt(i) == 'K' || input.charAt(i) == 'k')
				{
					num[i] = 20;
				}
				else if(input.charAt(i) == 'L' || input.charAt(i) == 'l')
				{
					num[i] = 21;
				}
				else if(input.charAt(i) == 'M' || input.charAt(i) == 'm')
				{
					num[i] = 22;
				}
				else if(input.charAt(i) == 'N' || input.charAt(i) == 'n')
				{
					num[i] = 23;
				}
				else if(input.charAt(i) == 'O' || input.charAt(i) == 'o')
				{
					num[i] = 24;
				}
				else if(input.charAt(i) == 'P' || input.charAt(i) == 'p')
				{
					num[i] = 25;
				}
				else if(input.charAt(i) == 'Q' || input.charAt(i) == 'q')
				{
					num[i] = 26;
				}
				else if(input.charAt(i) == 'R' || input.charAt(i) == 'r')
				{
					num[i] = 27;
				}
				else if(input.charAt(i) == 'S' || input.charAt(i) == 's')
				{
					num[i] = 28;
				}
				else if(input.charAt(i) == 'T' || input.charAt(i) == 't')
				{
					num[i] = 29;
				}
				else if(input.charAt(i) == 'U' || input.charAt(i) == 'u')
				{
					num[i] = 30;
				}
				else if(input.charAt(i) == 'V' || input.charAt(i) == 'v')
				{
					num[i] = 31;
				}
				else if(input.charAt(i) == 'W' || input.charAt(i) == 'w')
				{
					num[i] = 32;
				}
				else if(input.charAt(i) == 'X' || input.charAt(i) == 'x')
				{
					num[i] = 33;
				}
				else if(input.charAt(i) == 'Y' || input.charAt(i) == 'y')
				{
					num[i] = 34;
				}
				else if(input.charAt(i) == 'Z' || input.charAt(i) == 'z')
				{
					num[i] = 35;
				}
				else
				{
					temp1 = input.charAt(i) - 48;
					num[i] = temp1;
				}
			}

			// get base
			/*
			input = txt_base.getText();
			input = input.trim();
			*/
			//base = Integer.parseInt(input);

			// display converted answer
			ans = Convert(num, base, size);
			
			System.out.println("ans = "+ans);
			
			return ans;
    }
			
	public static int Convert(int num[], int base, int size)
	{
		int temp = 0;
		int i = 0;

		do
		{
			temp = (temp * base) + num[i];
			System.out.println(temp);
			i++;
		}while(i < size);

		return temp;
	}
     
 }
 
/*

public class FgsBase32
{
	public int GetEncode32Length(int bytes)
	{
	   int bits = bytes * 8;
	   int length = bits / 5;
	   if((bits % 5) > 0)
	   {
	      length++;
	   }
	   return length;
	}

	public int GetDecode32Length(int bytes)
	{
	   int bits = bytes * 5;
	   int length = bits / 8;
	   return length;
	}

	public static boolean Encode32Block(char* in5, char* out8)
	{
	      // pack 5 bytes
	      unsigned __int64 buffer = 0;
	      for(int i = 0; i < 5; i++)
	      {
			  if(i != 0)
			  {
				  buffer = (buffer << 8);
			  }
			  buffer = buffer | in5[i];
	      }
	      // output 8 bytes
	      for(int j = 7; j >= 0; j--)
	      {
			  buffer = buffer << (24 + (7 - j) * 5);
			  buffer = buffer >> (24 + (7 - j) * 5);
			  char c = (char)(buffer >> (j * 5));
			  // self check
			  if(c >= 32) return false;
			  out8[7 - j] = c;
	      }
		  return true;
	}

	public boolean Encode32(char* in, int inLen, char* out)
	{
	   if((in == 0) || (inLen <= 0) || (out == 0)) return false;
	
	   int d = inLen / 5;
	   int r = inLen % 5;
	
	   unsigned char outBuff[8];
	
	   for(int j = 0; j < d; j++)
	   {
	      if(!Encode32Block(&in[j * 5], &outBuff[0])) return false;
	      memmove(&out[j * 8], &outBuff[0], sizeof(unsigned char) * 8);
	   }
	
	   unsigned char padd[5];
	   memset(padd, 0, sizeof(unsigned char) * 5);
	   for(int i = 0; i < r; i++)
	   {
	      padd[i] = in[inLen - r + i];
	   }
	   if(!Encode32Block(&padd[0], &outBuff[0])) return false;
	   memmove(&out[d * 8], &outBuff[0], sizeof(unsigned char) * GetEncode32Length(r));
	
	   return true;
	}

	public static bool Decode32Block(unsigned char* in8, unsigned char* out5)
	{
	      // pack 8 bytes
	      unsigned __int64 buffer = 0;
	      for(int i = 0; i < 8; i++)
	      {
			  // input check
			  if(in8[i] >= 32) return false;
			  if(i != 0)
			  {
				  buffer = (buffer << 5);
			  }
			  buffer = buffer | in8[i];
	      }
	      // output 5 bytes
	      for(int j = 4; j >= 0; j--)
	      {
			  out5[4 - j] = (unsigned char)(buffer >> (j * 8));
	      }
		  return true;
	}

	public bool Decode32(unsigned char* in, int inLen, unsigned char* out)
	{
	   if((in == 0) || (inLen <= 0) || (out == 0)) return false;
	
	   int d = inLen / 8;
	   int r = inLen % 8;
	
	   unsigned char outBuff[5];
	
	   for(int j = 0; j < d; j++)
	   {
	      if(!Decode32Block(&in[j * 8], &outBuff[0])) return false;
	      memmove(&out[j * 5], &outBuff[0], sizeof(unsigned char) * 5);
	   }
	
	   unsigned char padd[8];
	   memset(padd, 0, sizeof(unsigned char) * 8);
	   for(int i = 0; i < r; i++)
	   {
	      padd[i] = in[inLen - r + i];
	   }
	   if(!Decode32Block(&padd[0], &outBuff[0])) return false;
	   memmove(&out[d * 5], &outBuff[0], sizeof(unsigned char) * GetDecode32Length(r));
	
	   return true;
	}
	
	public bool Map32(unsigned char* inout32, int inout32Len, unsigned char* alpha32)
	{
		if((inout32 == 0) || (inout32Len <= 0) || (alpha32 == 0)) return false;
		for(int i = 0; i < inout32Len; i++)
		{
			if(inout32[i] >=32) return false;
			inout32[i] = alpha32[inout32[i]];
		}
		return true;
	}
	
	public static void ReverseMap(unsigned char* inAlpha32, unsigned char* outMap)
	{
		memset(outMap, 0, sizeof(unsigned char) * 256);
		for(int i = 0; i < 32; i++)
		{
			outMap[(int)inAlpha32[i]] = i;
		}
	}
	
	public bool Unmap32(unsigned char* inout32, int inout32Len, unsigned char* alpha32)
	{
		if((inout32 == 0) || (inout32Len <= 0) || (alpha32 == 0)) return false;
		unsigned char rmap[256];
		ReverseMap(alpha32, rmap);
		for(int i = 0; i < inout32Len; i++)
		{
			inout32[i] = rmap[(int)inout32[i]];
		}
		return true;
	}
}
*/