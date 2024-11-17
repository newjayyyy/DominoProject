package Domino;

public class Tile {
	int leftnum;
	int rightnum;
	String imagename;
	
	
	
	void print() {
		System.out.printf("(%d,%d)", leftnum, rightnum);
	}
	void printgametile() {
		System.out.printf("[%d,%d]",leftnum,rightnum);
	}
	
	boolean tilematches(int l, int r) {
		if(leftnum==l||leftnum==r||rightnum==l||rightnum==r) {
			return true;
		}
		return false;
	}
	int tilesum() {
		return leftnum+rightnum;
	}
	boolean tilematches_left(int l) {
		if(leftnum==l||rightnum==l) return true;
		return false;
	}
	boolean tilematches_right(int r) {
		if(leftnum==r||rightnum==r) return true;
		return false;
	}
	
	
	void switching() {
		int tmp=leftnum;
		leftnum=rightnum;
		rightnum=tmp;
		
	}
}
