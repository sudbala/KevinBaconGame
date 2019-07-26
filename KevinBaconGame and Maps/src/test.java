import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class test {

	public static void main(String[] args) {
		ArrayList<Integer> buree = new ArrayList<Integer>();
		
		buree.add(2);
		buree.add(1);
		buree.add(123);
		buree.add(23);
		buree.add(1231);
		buree.add(94);
		buree.add(34);
		buree.add(0);
		buree.add(45);
		buree.add(54);
		buree.add(26);
		buree.add(28);
		
		System.out.println(buree);
		Collections.sort(buree);
		System.out.println(buree);
		Collections.sort(buree, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return -1*(o1-o2);
			}
		});
		System.out.println(buree);
	}

}
