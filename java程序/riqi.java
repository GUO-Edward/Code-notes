import java.util.Scanner;
class  riqi
{
	public static void main(String[] args) 
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("������year");
		int year = scan.nextInt();
		System.out.println("������month");
		int month = scan.nextInt();
		System.out.println("������dat");
		int day = scan.nextInt();
		int sunDays=0;
		switch(month){
			case 12:
				sunDays += 30;
			case 11:
				sunDays += 31;
			case 10:
				sunDays += 30;
			case 9:
				sunDays += 31;
			case 8:
				sunDays += 31;
			case 7:
				sunDays += 30;
			case 6:
				sunDays += 31;
			case 5:
				sunDays += 30;
			case 4:
				sunDays += 31;
			case 3:
				if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
				{
					sunDays += 29;
				}
				else{
					sunDays += 28;
				}
			case 2:
				sunDays += 31;
			case 1:
				sunDays += day;
		}
		System.out.println(year + "��" + month + "��" + day + "���ǵ���ĵ�" + sunDays + "��");
	}
}
