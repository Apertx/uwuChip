package apertx.uwuchip;
import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;

public class Main extends Activity{
	final int PORT_X=2;
	final int PORT_Y=2;
	final int MEM_SIZE=64;

	EditText edit;
	View io;
	Rect[] rio;
	boolean[] sio;
	boolean dio;
	boolean emul;
	Paint paint;
	int ports;
	byte PC;
	boolean W;
	byte[] bytecode;
	String[] opcode = {"nop","not","clr","set","rep","repz","-","-","ina","inb","inc","ind","outa","outb","outc","outd"};
	String[] ophelp = {
		"Код: 0000\nОписание: Нет операции\n\nДействие: PC + 1",
		"Код: 0001\nОписание: Инвертировать бит W\n\nДействие: W = ~W; PC + 1",
		"Код: 0010\nОписание: Установить бит W в 0\n\nДействие: W = 0; PC + 1",
		"Код: 0011\nОписание: Установить бит W в 1\n\nДействие: W = 1; PC + 1",
		"Код: 0100\nОписание: Если бит W = 1, повторяет предыдущую операцию\n\nДействие: if (W == 1) PC - 1 else PC + 1",
		"Код: 0101\nОписание: Если бит W = 0, повторяет предыдущую операцию\n\nДействие: if (W == 0) PC - 1 else PC + 1",
		"Код: 0110\nОписание: Не используется\n\nДействие: PC + 1",
		"Код: 0111\nОписание: Не используется\n\nДействие: PC + 1",
		"Код: 1000\nОписание: Получить значение порта 1 в бит W\n\nДействие: 1 -> W; PC + 1",
		"Код: 1001\nОписание: Получить значение порта 2 в бит W\n\nДействие: 2 -> W; PC + 1",
		"Код: 1010\nОписание: Получить значение порта 3 в бит W\n\nДействие: 3 -> W; PC + 1",
		"Код: 1011\nОписание: Получить значение порта 4 в бит W\n\nДействие: 4 -> W; PC + 1",
		"Код: 1100\nОписание: Отправить значение в порт 1 из бита W\n\nДействие: W -> 1; PC + 1",
		"Код: 1101\nОписание: Отправить значение в порт 2 из бита W\n\nДействие: W -> 2; PC + 1",
		"Код: 1110\nОписание: Отправить значение в порт 3 из бита W\n\nДействие: W -> 3; PC + 1",
		"Код: 1111\nОписание: Отправить значение в порт 4 из бита W\n\nДействие: W -> 4; PC + 1"
	};
	CharSequence[] menus = {"Run","New","Open","Save","Save as","Help","Exit"};
	String file;

	@Override protected void onCreate(Bundle bu){
		super.onCreate(bu);
		ports=PORT_X*PORT_Y;
		emul=false;
		edit=new EditText(this);
		edit.setGravity(0);
		edit.setBackgroundColor(0);
		edit.setTextColor(0xFF7F7F7F);
		edit.setTypeface(Typeface.MONOSPACE);
		rio=new Rect[ports];
		sio=new boolean[ports];
		for(int i = 0; i<ports; i+=1) sio[i]=false;
		dio=false;
		paint=new Paint();
		paint.setColor(0xFF7F7F7F);
		bytecode=new byte[MEM_SIZE];
		io=new View(this) {
			@Override protected void onDraw(Canvas c){
				for(int i = 0; i<ports; i+=1)
					if(sio[i]) c.drawRect(rio[i],paint);
			}
			@Override public boolean onTouchEvent(MotionEvent e){
				int x = (int)e.getX();
				int y = (int)e.getY();
				for(int i = 0; i<ports; i+=1)
					if(rio[i].contains(x,y)&&e.getAction()==MotionEvent.ACTION_DOWN&&sio[i]==false){
						sio[i]=true;
						dio=true;
					}else if(rio[i].contains(x,y)&&e.getAction()==MotionEvent.ACTION_UP&&sio[i]){
						sio[i]=false;
						dio=true;
					}
				return true;
			}
			@Override protected void onLayout(boolean c,int l,int t,int r,int b){
				if(c){
					int width = r-l;
					int height = b-t;
					for(int y = 0; y<PORT_Y; y+=1)
						for(int x = 0; x<PORT_X; x+=1)
							rio[y*PORT_X+x]=new Rect(width/PORT_X*x,height/PORT_Y*y,width/PORT_X*(x+1),height/PORT_Y*(y+1));
				}
			}
		};
		setContentView(edit);
	}
	@Override public boolean onKeyDown(int k,KeyEvent e){
		if(k==KeyEvent.KEYCODE_BACK){
			emul=!emul;
			if(emul)
				new AlertDialog.Builder(this).setItems(menus,new AlertDialog.OnClickListener(){
						@Override public void onClick(DialogInterface d,int p){
							switch(p){
								case 0:
									W=false;
									for(int i = 0; i<ports; i+=1) sio[i]=false;
									for(int i = 0; i<MEM_SIZE; i+=1) bytecode[i]=0;
									if(edit.getText().length()!=0){
										String[] codeText = edit.getText().toString().replaceAll("\n\n","\n").split("\n");
										for(byte j = 0; j<codeText.length; j++){
											String codeWord = codeText[j].trim().toLowerCase();
											for(byte i = 0; i<16; i++)
												if(codeWord.equals(opcode[i])){
													bytecode[j]=i;
													break;
												}
										}
									}
									setContentView(io);
									new Thread(new Runnable() {
											@Override public void run(){
												while(emul){
													if(PC==MEM_SIZE) PC=0;
													switch(bytecode[PC]){
														case 0: PC+=1; break;
														case 1: W=!W; PC+=1; break;
														case 2: W=false; PC+=1; break;
														case 3: W=true; PC+=1; break;
														case 4: if(W) PC-=1; else PC+=1; break;
														case 5: if(W==false&&PC!=0) PC-=1; else PC+=1; break;
														case 6: PC+=1; break;
														case 7: PC+=1; break;
														case 8: W=sio[0]; PC+=1; break;
														case 9: W=sio[1]; PC+=1; break;
														case 10: W=sio[2]; PC+=1; break;
														case 11: W=sio[3]; PC+=1; break;
														case 12: if(sio[0]!=W){sio[0]=W; dio=true;} PC+=1; break;
														case 13: if(sio[1]!=W){sio[1]=W; dio=true;} PC+=1; break;
														case 14: if(sio[2]!=W){sio[2]=W; dio=true;} PC+=1; break;
														case 15: if(sio[3]!=W){sio[3]=W; dio=true;} PC+=1; break;
													}
													try{Thread.sleep(1);}catch(InterruptedException e){}
													if(dio){
														io.postInvalidate();
														dio=false;
													}
												}
											}
										}).start();
									break;
								case 1:
									edit.setText("");
									file=null;
									break;
								case 2:
									startActivityForResult(new Intent(Main.this,Files.class),1);
									break;
								case 3:
									if(file==null)
										startActivityForResult(new Intent(Main.this,Files.class),2);
									else
										try{
											OutputStream os = openFileOutput(file,0);
											os.write(edit.getText().toString().getBytes());
											os.close();
										}catch(Exception e){}
									break;
								case 4:
									startActivityForResult(new Intent(Main.this,Files.class),2);
									break;
								case 5:
									new AlertDialog.Builder(Main.this).
										setTitle("Помощь").setItems(opcode,new DialogInterface.OnClickListener(){
											@Override public void onClick(DialogInterface di,int pi){
												new AlertDialog.Builder(Main.this).
													setTitle(opcode[pi]).setMessage(ophelp[pi]).
													setCancelable(false).
													setPositiveButton("ОК",null).show();
											}
										}).show();
									break;
								case 6:
									finish();
									break;
							}
						}
					}).show();
			else setContentView(edit);
			return true;
		}else return super.onKeyDown(k,e);
	}
	@Override protected void onActivityResult(int q,int r,Intent d){
		super.onActivityResult(q,r,d);
		if(r==RESULT_OK) {
			file=d.getStringExtra("file");
			if(q==1)
				try{
					InputStream is = openFileInput(file);
					byte[] buf = new byte[is.available()];
					is.read(buf);
					is.close();
					edit.setText(new String(buf));
				}catch(Exception e){}
			else if(q==2)
				try{
					OutputStream os = openFileOutput(file,0);
					os.write(edit.getText().toString().getBytes());
					os.close();
				}catch(Exception e){}
		}
	}
}
