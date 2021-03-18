/////
//Zachary Perry
//Project #1 Computer Arch.
//MIPS Disassembler in java
//02/25/2020
//////
public class Main {

	public static void main(String[] args)
	{
		///Set the intial address to 4 spots behind the starting address, increment by 4 spots///
		/// in memory every loop through the instructions.///////////////////////////////////////
		
		int hexOp = 0;
		int address = 0X9A040 - 4;
		int[] instruct = new int[] {0X032BA020,0X8CE90014,0X12A90003,0X022DA822,0XADB30020, 0X02697824, 
				0XAE8FFFF4, 0X018C6020, 0X02A4A825, 0X158FFFF7, 0X8ECDFFF0};
		
		///run through a for loop of the array of instructions, have the legnth of the for loop///
		///match the length of the array./////////////////////////////////////////////////////////
		
		for (int i = 0; i < instruct.length; i++) 
		{
			//If bit 8(opcode of both formats) is a 0, then use the R method, if not use I format//
			
			address = address +4;
			hexOp = instruct[i] >>>26;
			if(hexOp == 0)
			{
				formatR(instruct[i], address);
			}
			else {
				formatI(instruct[i], hexOp, address);
				 }
		}
	}
	
	public static void formatR(int instruct, int address)
	{
		// masks for R format//
		int maskSrc1 = 0x03E00000;
		int maskSrc2 = 0x001f0000;
		int maskDst  = 0x0000F800;
		int maskFunc = 0x0000003F;
		
		//get the opcode, destination, and two source registers, print to console//
		System.out.print(String.format("0x%08x", address) + " " );
		System.out.print(determineOPCode(calculateField(instruct, maskFunc, 0)));
		System.out.print(calculateField(instruct, maskDst, 11) + ",$");
		System.out.print(calculateField(instruct, maskSrc1,21) + ",$");
		System.out.println(calculateField(instruct, maskSrc2, 16));
		
	}
		
	
	private static void formatI(int instruct, int hexOp, int address) 
	{
		// masks for I format//
		int maskSrc1    = 0X3E00000;
		int maskSrc2Dst = 0x1F0000;
		int offsetmask  = 0xFFFF;
		
		
		//process if it is a branch or not//
		
		if (hexOp == 4 || hexOp == 5)
		{
			//branch instructions, printing to console//
			System.out.print(String.format("0x%08x ", address) + " " );
			System.out.print(determineOPCode(hexOp));
			
			
			System.out.print(calculateField(instruct,maskSrc2Dst, 16) + ",$");
			System.out.print(calculateField(instruct, maskSrc1, 21) + " address ");
			System.out.println(String.format("0x%08x ", calculateOffset(instruct, offsetmask, address)*4 + address + 4));
		}
		
		else  
		{
			//non branch instruction I format, printing to console//
			System.out.print(String.format("0x%08x ", address) + " " );
			System.out.print(determineOPCode(hexOp));
			
			
			
			System.out.print(calculateField(instruct, maskSrc2Dst, 16) + ",");
			System.out.print(calculateOffset(instruct, offsetmask,address)+ "($");
			System.out.println(calculateField(instruct, maskSrc1, 21) + ")");
			
		}
	}
	
	
	public static int calculateField (int instruct, int mask, int numBits)
	{
		//Take the current instruction(instruct), use the defined mask(mask) on the string of binary///
		//then shift the binary over the amount(numbits) needed to shift it to the end///
		int field = 0;
		field = (instruct & mask);
		field = field >>> numBits;
		return field;
	}
	

	public static short calculateOffset (int instruct, int offsetmask, int address)
	{
		//Use a bitmask to isolate the offset, convert it to a short at this step so that it handles negative bits//
		
		short field = (short) (instruct & offsetmask);
		return field;
	}
	
	
	public static String determineOPCode (int hexOp)
	{
		/// Simple Switch Statement for the hexOP that returns a string with the type of instruction///
		String opCode = "";
		switch (hexOp)
		{
		case 32: opCode = " add $"; break;
		case 34: opCode = " sub $"; break;
		case 36: opCode = " and $"; break;
		case 37: opCode = " or  $"; break;
		case 42: opCode = " slt $"; break;
		case 35: opCode = "lw  $"; break;
		case 43: opCode = "sw  $"; break;
		case 5:  opCode = "bne $"; break;
		case 4:  opCode = "beq $"; break;
		
		
		default: 
			System.out.print("There is something wrong with your opcode");
			//System.exit(0);
		}
		return opCode;
	}

}
