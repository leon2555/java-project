public class Rectangulator {
	public static void main(String[] args) {
		int length = Integer.parseInt(args[0]);
		int width = Integer.parseInt(args[1]);

		Rectangle myREctangle = new Rectangle(length, width);

		String output = String.format("**** Your Rectangle ****\n\nLength: %d\nWidth: %d\nArea: %d\nPermeter: %d\n\n", myREctangle.length, myREctangle.width, myREctangle.getArea(), myREctangle.getPerimeter());
		
		System.out.println(output);
	}
}