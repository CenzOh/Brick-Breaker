package projectAlba;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.applet.*;
import java.net.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;



public class GamePlay extends JPanel implements KeyListener, ActionListener{

	private static final long serialVersionUID = 1L;
	private boolean play=false;
	private int score=0;
	private int lives=3;
	private int level = 1;
	private boolean win = false;
	private int totalBricks= 21;
	private int rows = 3;
	private int colomns = 7;
	private Timer timer;
	private int delay=8;
	
	private int playerX=310;
	
	private int ballposX=120;
	private int ballposY=350;
	private int ballXdir= -1;
	private int ballXdirLeft = -1;
	private int ballXdirRight = 1;
	
	private int ballYdir=-2;
	private boolean space =true;
	private int barSpeed = 20;
//private boolean hasName = false;
	//
private String levelUp = "file:C:\\temp\\Java\\Workspace\\CSC330\\src\\projectAlba/Sounds/smb_1-up.wav";
private String breakBrick = "file:C:\\temp\\Java\\Workspace\\CSC330\\src\\projectAlba\\Sounds/smb_kick.wav";
private String hitPaddle = "file:C:\\temp\\Java\\Workspace\\CSC330\\src\\projectAlba/Sounds/smb_bump.wav";
private String gameOver = "file:C:\\temp\\Java\\Workspace\\CSC330\\src\\projectAlba/Sounds/smb_gameover.wav";
private String lostLife = "file:C:\\temp\\Java\\Workspace\\CSC330\\src\\projectAlba/Sounds/smb_bowserfalls.wav";
	
	private MapGen map;
	private PlayerName user;

			
	public GamePlay() {
		
		map=new MapGen(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer= new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {


		//background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		//draw map
		map.draw((Graphics2D) g);
		
		//borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//levels 
		g.setColor(Color.magenta);	
		g.setFont(new Font("serif", Font.BOLD, 25));	
		g.drawString("Level: " + level, 550, 30 ); // right side
		
		//scores
		g.setColor(Color.white);
		g.setFont(new Font("serif",Font.BOLD,25));
		g.drawString("Score: "+score, 10, 30 ); // left side
		
		//lives
		g.setColor(Color.cyan);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Lives: "+lives, 310, 30); // middle
		
		
		//the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		//the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);

		if(totalBricks<=0) {
			
			win = true;
			play=false;
			//playMusic(levelUp);
			ballXdir=0;
			ballYdir=0;
			g.setColor(Color.green);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("You Won!", 265, 300);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Score: "+score, 280, 325);
			
	
			g.setFont(new Font("serif", Font.BOLD, 20)); 	// restart
			g.drawString("Press Enter to Start a New Game!",210,  350);
			g.setFont(new Font("serif", Font.BOLD, 20)); 
			g.drawString("Or press C to advance to the next level!", 190,  370);
			
		}
	
		
		 if((lives == 2 && space ==true) || lives == 1 && space == false  ) {
			play=false;
			
			ballXdir=0;
			ballYdir=0;
			
			ballposX=120;
			ballposY=350;
			//playMusic(lostLife);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Lives left: "+lives, 280, 325);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Space to Continue", 230, 350);
			
		
		} 
		
		
		if(lives==0 ){
			play=false;
			win = false;
			
			//playMusic(gameOver);
			g.setColor(Color.red);
			g.setFont(new Font("serif",Font.BOLD,30));
			g.drawString("Game Over!", 250, 300);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Scores:"+score, 280, 325);
			
			g.setFont(new Font("serif",Font.BOLD,20));
			g.drawString("Press Enter to Start a New Game!", 190, 350);
			
			
		}
		
		
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		timer.start();
		if(play) {
			
			if(new Rectangle (ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX,550,100,8))) {
				ballYdir =-ballYdir;
				//ballXdir = ballXdirRight; // ISSUE: Always bouncing to the right
				playMusic(hitPaddle);
			//	if(new Rectangle (ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX,550,30,8))) {
			//		ballXdir = -ballXdirLeft; // FIX
			//	}
			}
			
		A:	for(int i=0;i<map.map.length;i++) {
				for(int j=0;j<map.map[0].length;j++) {
					if(map.map[i][j]>0) {
						int brickX=j*map.brickWidth+80;
						int brickY=i*map.brickHeight+50;
						int brickWidth= map.brickWidth;
						int brickHeight=map.brickHeight;
						
						Rectangle rect= new Rectangle(brickX,brickY,brickWidth, brickHeight);
						Rectangle ballRect=new Rectangle (ballposX, ballposY,20, 20);
						Rectangle brickRect= rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0,i,j);
							totalBricks--;
							score+=5;
							playMusic(breakBrick);
							if(ballposX+19 <=brickRect.x || ballposX+1>=brickRect.x + brickRect.width) {
								ballXdir=-ballXdir;
							} else {
								ballYdir=-ballYdir;
							}
							
							break A;
						}
						
					}
				}
				
			}
			
			ballposX+=ballXdir;
			ballposY+=ballYdir;
			if(ballposX <0) {
				ballXdir= -ballXdir;
				playMusic(hitPaddle);
			}
			if(ballposY <0) {
				ballYdir= -ballYdir;
				playMusic(hitPaddle);
			}
			if(ballposX >670) {
				ballXdir= -ballXdir;
				playMusic(hitPaddle);
			}
			if(ballposY>570) {
				lives--;

				
		
				 
			}
			if(totalBricks <= 0 ){
				playMusic(levelUp);
			}
			 if((lives == 2 && space ==true) || lives == 1 && space == false  ) {
				 playMusic(lostLife);
			 }
			 if(lives == 0) {
				 playMusic(gameOver);
			 }
		}
		repaint();
		
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
			if(playerX >=600) {
				playerX=600;
			}else {
				moveRight();
			}
		}
		if (e.getKeyCode()==KeyEvent.VK_LEFT) {
			if(playerX<10) {
				playerX=10;
			} else {
				moveLeft();
			}
			
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) { // continue 
			if(!play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				space = !space;
			}
	
		}
			if(e.getKeyCode() == KeyEvent.VK_C) {
				if(win == true) {	
					play = true;
					ballposX = 120;
					ballposY = 350;
					ballXdir = -1;
					ballYdir = -2;
					playerX = 310;
					totalBricks=21;//continue the game but increase the level
					level++;
					ballXdir--;		//also increasing difficulty
					ballXdirLeft--;
					ballXdirRight++;
					ballYdir--;
					barSpeed += 7;
					rows++;
					colomns++;
					map=new MapGen(rows,colomns);
					totalBricks = rows*colomns;
					repaint();
					
					
			}

		}
		
		if(e.getKeyCode()==KeyEvent.VK_ENTER) {
			if(!play) {
				play=true;
				ballposX=120;
				ballposY=350;
				ballXdir=-1;
				ballXdirLeft = -1;
				ballXdirRight = 1;
				ballYdir=-2;
				playerX=310;
				score=0;
				totalBricks=21;
				lives=3;
				level = 1;
				map=new MapGen(3,7);
				repaint();
				
			}
		}
	}
	public void moveRight() {
		play=true;
		playerX+=barSpeed;
	}
	public void moveLeft() {
		play=true;
		playerX-=barSpeed;
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
	
	public static void playMusic(String filename) {
		
		try {
		AudioClip clip = Applet.newAudioClip(
				
		new URL(filename));
		
		clip.play();
		} catch (MalformedURLException murle) {
		System.out.println(murle);
		}
		
		
	}

}
