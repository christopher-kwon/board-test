package net.board.action;

public class test {
	
	public void  main(String[] args) {
		
		String hobbys="공부 피아노 게임 음악감상";
		
		String[] hobby = hobbys.split(" ");
		
		for(String hobbyss : hobby) {
			System.out.println(hobbyss);
		}
	}

}
