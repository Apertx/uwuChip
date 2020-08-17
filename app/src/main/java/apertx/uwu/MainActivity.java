package apertx.uwu;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.LinearLayout.*;
import java.io.*;
import android.text.*;

public class MainActivity extends Activity {
	LinearLayout mml;
	EditText edit;
	LinearLayout ml;
	String[] opCodes= {"nop","not","clr","set","rep","repz","-","-","ina","inb","inc","ind","outa","outb","outc","outd"};
	String[] opData = {
		"Код: 0000\nОписание: Нет операции\n\nДействие: PC + 1",
		"Код: 0001\nОписание: Инвертировать бит W\n\nДействие: W = ~W; PC + 1",
		"Код: 0010\nОписание: Установить бит W в 0\n\nДействие: W = 0; PC + 1",
		"Код: 0011\nОписание: Установить бит W в 1\n\nДействие: W = 1; PC + 1",
		"Код: 0100\nОписание: Если бит W = 1, повторяет предыдущую операцию\n\nДействие: if (W == 1) PC - 1 else PC + 1",
		"Код: 0101\nОписание: Если бит W = 0, повторяет предыдущую операцию\n\nДействие: if (W == 0) PC - 1 else PC + 1",
		"Код: 0110\nОписание: Не используется\n\nДействие:",
		"Код: 0111\nОписание: Не используется\n\nДействие:",
		"Код: 1000\nОписание: Получить значение порта 1 в бит W\n\nДействие: 1 -> W; PC + 1",
		"Код: 1001\nОписание: Получить значение порта 2 в бит W\n\nДействие: 2 -> W; PC + 1",
		"Код: 1010\nОписание: Получить значение порта 3 в бит W\n\nДействие: 3 -> W; PC + 1",
		"Код: 1011\nОписание: Получить значение порта 4 в бит W\n\nДействие: 4 -> W; PC + 1",
		"Код: 1100\nОписание: Отправить значение в порт 1 из бита W\n\nДействие: W -> 1; PC + 1",
		"Код: 1101\nОписание: Отправить значение в порт 2 из бита W\n\nДействие: W -> 2; PC + 1",
		"Код: 1110\nОписание: Отправить значение в порт 3 из бита W\n\nДействие: W -> 3; PC + 1",
		"Код: 1111\nОписание: Отправить значение в порт 4 из бита W\n\nДействие: W -> 4; PC + 1"
	};
	boolean a;
	boolean b;
	boolean c;
	boolean d;
	boolean W;
	byte PC;
	View va;
	View vb;
	View vc;
	View vd;
	byte[] byteCode;
	boolean run;
	boolean ready;
	int last_lines;
	static final int ID_MENU_COMPILE = 0;
	static final int ID_MENU_NEW = 1;
	static final int ID_MENU_OPEN = 2;
	static final int ID_MENU_SAVE = 3;
	static final int ID_MENU_EXPORT = 4;
	static final int ID_MENU_HELP = 5;
	static final int ID_MENU_EXIT = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mml = new LinearLayout(this);
		ScrollView sv = new ScrollView(this);
		LinearLayout sl = new LinearLayout(this);
		LayoutParams le = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		edit = new EditText(this);
		edit.setBackgroundColor(Color.TRANSPARENT);
		edit.setTextSize(16);
		edit.setTypeface(Typeface.MONOSPACE);
		edit.setBackgroundColor(Color.BLACK);
		final TextView tv = new TextView(this);
		tv.setTextSize(16);
		tv.setTypeface(Typeface.MONOSPACE);
		tv.setEms(2);
		mml.setLayoutParams(le);
		sv.setLayoutParams(le);
		sl.setLayoutParams(le);
		edit.setLayoutParams(le);
		sl.addView(tv);
		sl.addView(edit);
		sv.addView(sl);
		mml.addView(sv);

		ml = new LinearLayout(this);
		LinearLayout tl = new LinearLayout(this);
		LinearLayout bl = new LinearLayout(this);
		ml.setOrientation(LinearLayout.VERTICAL);
		va = new View(this);
		vb = new View(this);
		vc = new View(this);
		vd = new View(this);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.weight = 1.0f;
		tl.setLayoutParams(lp);
		bl.setLayoutParams(lp);
		lp.setMargins(4, 2, 4, 2);
		va.setLayoutParams(lp);
		vb.setLayoutParams(lp);
		vc.setLayoutParams(lp);
		vd.setLayoutParams(lp);
		tl.addView(va);
		tl.addView(vb);
		bl.addView(vc);
		bl.addView(vd);
		ml.addView(tl);
		ml.addView(bl);
		va.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View p0, MotionEvent p1) {
					if (p1.getAction() == MotionEvent.ACTION_DOWN) {
						a = true;
						va.setBackgroundColor(Color.GREEN);
					}
					if (p1.getAction() == MotionEvent.ACTION_UP) {
						a = false;
						va.setBackgroundColor(Color.RED);
					}
					return true;
				}
			});
		vb.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View p0, MotionEvent p1) {
					if (p1.getAction() == MotionEvent.ACTION_DOWN) {
						b = true;
						vb.setBackgroundColor(Color.GREEN);
					}
					if (p1.getAction() == MotionEvent.ACTION_UP) {
						b = false;
						vb.setBackgroundColor(Color.RED);
					}
					return true;
				}
			});
		vc.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View p0, MotionEvent p1) {
					if (p1.getAction() == MotionEvent.ACTION_DOWN) {
						c = true;
						vc.setBackgroundColor(Color.GREEN);
					}
					if (p1.getAction() == MotionEvent.ACTION_UP) {
						c = false;
						vc.setBackgroundColor(Color.RED);
					}
					return true;
				}
			});
		vd.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View p0, MotionEvent p1) {
					if (p1.getAction() == MotionEvent.ACTION_DOWN) {
						d = true;
						vd.setBackgroundColor(Color.GREEN);
					}
					if (p1.getAction() == MotionEvent.ACTION_UP) {
						d = false;
						vd.setBackgroundColor(Color.RED);
					}
					return true;
				}
			});

		last_lines = 1;
		tv.setText("1");
		setContentView(mml);
		ready = true;
		run = false;
		edit.addTextChangedListener(new TextWatcher() {
				@Override public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override public void onTextChanged(CharSequence p1, int p2, int p3, int p4) {}
				@Override
				public void afterTextChanged(Editable p1) {
					int lines = 1;
					int offset = -1;
					String _edit = edit.getText().toString();
					while ((offset = _edit.indexOf('\n', offset + 1)) != -1)
						lines += 1;
					if (last_lines != lines) {
						last_lines = lines;
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < lines; i++)
							sb.append(i + 1).append('\n');
						tv.setText(sb.toString());
					}
				}
			});
	}

	byte[] compileCode(String _codeText) {
		byte[] _byteCode = new byte[32];
		ready = true;
		if (_codeText.length() != 0) {
			String[] codeText = _codeText.replaceAll("\n\n", "\n").split("\n");
			for (byte j = 0; j < codeText.length; j++) {
				String codeWord = codeText[j];
				_byteCode[j] = 99;
				for (byte i = 0; i < 16; i++)
					if (codeWord.equals(opCodes[i])) {
						_byteCode[j] = i;
						break;
					}
				if (_byteCode[j] == 99) {
					ready = false;
					Toast.makeText(this, "Ошибка в строке " + (j + 1) + ", неизвестная команда: " + codeWord, 1).show();
					break;
				}
			}
		}
		return _byteCode;
	}

	class doCode implements Runnable {
		byte[] byteCode;
		doCode(byte[] _byteCode) {
			byteCode = _byteCode;
			a = false;
			b = false;
			c = false;
			d = false;
			W = false;
			PC = 0;
			va.setBackgroundColor(Color.RED);
			vb.setBackgroundColor(Color.RED);
			vc.setBackgroundColor(Color.RED);
			vd.setBackgroundColor(Color.RED);
		}

		@Override
		public void run() {
			while (run) {
				if (PC == 32) PC = 0;
				switch (byteCode[PC]) {
					case 0: PC += 1; break;
					case 1: W = !W; PC += 1; break;
					case 2: W = false; PC += 1; break;
					case 3: W = true; PC += 1; break;
					case 4: if (W) PC -= 1; else PC += 1; break;
					case 5: if (W == false && PC != 0) PC -= 1; else PC += 1; break;
					case 8: W = a; PC += 1; break;
					case 9: W = b; PC += 1; break;
					case 10: W = c; PC += 1; break;
					case 11: W = d; PC += 1; break;
					case 12:
						if (a != W) {
							a = W;
							va.post(new Runnable() {
									@Override public void run() {
										if (a) va.setBackgroundColor(Color.GREEN); else va.setBackgroundColor(Color.RED);
									}
								});
						}
						PC += 1;
						break;
					case 13:
						if (b != W) {
							b = W;
							vb.post(new Runnable() {
									@Override public void run() {
										if (b) vb.setBackgroundColor(Color.GREEN); else vb.setBackgroundColor(Color.RED);
									}
								});
						}
						PC += 1;
						break;
					case 14:
						if (c != W) {
							c = W;
							vc.post(new Runnable() {
									@Override public void run() {
										if (c) vc.setBackgroundColor(Color.GREEN); else vc.setBackgroundColor(Color.RED);
									}
								});
						}
						PC += 1;
						break;
					case 15:
						if (d != W) {
							d = W;
							vd.post(new Runnable() {
									@Override public void run() {
										if (d) vd.setBackgroundColor(Color.GREEN); else vd.setBackgroundColor(Color.RED);
									}
								});
						}
						PC += 1;
						break;

				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, ID_MENU_COMPILE, Menu.NONE, "Запуск");
		menu.add(Menu.NONE, ID_MENU_NEW, Menu.NONE, "Новый");
		menu.add(Menu.NONE, ID_MENU_OPEN, Menu.NONE, "Открыть");
		menu.add(Menu.NONE, ID_MENU_SAVE, Menu.NONE, "Сохранить");
		menu.add(Menu.NONE, ID_MENU_EXPORT, Menu.NONE, "Экспорт");
		menu.add(Menu.NONE, ID_MENU_HELP, Menu.NONE, "Помощь");
		menu.add(Menu.NONE, ID_MENU_EXIT, Menu.NONE, "Выход");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case ID_MENU_NEW:
				edit.setText("");
				break;
			case ID_MENU_COMPILE:
				byteCode = compileCode(edit.getText().toString());
				if (ready && run == false) {
					setContentView(ml);
					new Thread(new doCode(byteCode)).start();
					run = true;
				}
				break;
			case ID_MENU_HELP:
				new AlertDialog.Builder(this).
					setTitle("Помощь").
					setItems(opCodes, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface p0, int p1) {
							new AlertDialog.Builder(MainActivity.this).
								setTitle(opCodes[p1]).setMessage(opData[p1]).
								setCancelable(false).
								setPositiveButton("ОК", null).show();
						}
					}).show();
				break;
			case ID_MENU_EXIT:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (run) {
			run = false;
			setContentView(mml);
		} else super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			OutputStream os = openFileOutput("temp.asm", MODE_PRIVATE);
			os.write(edit.getText().toString().getBytes());
			os.close();
		} catch (Exception e) {}
	}
}
