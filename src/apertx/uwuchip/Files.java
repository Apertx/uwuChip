package apertx.uwuchip;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.view.View.*;
import android.content.*;
import android.widget.AdapterView.*;
import java.io.*;
import java.util.*;

public class Files extends Activity{
	@Override protected void onCreate(Bundle b){
		super.onCreate(b);
		final List<String> files = new ArrayList<String>();
		String[] fls = getFilesDir().list();
		for (int i = 0; i<fls.length; i+=1)
			files.add(fls[i]);
		final EditText edit = new EditText(this);
		edit.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
		edit.setSingleLine();
		final ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,files);
		ListView list = new ListView(this);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener(){
				@Override public void onItemClick(AdapterView<?> a,View v,int p,long id){
					edit.setText(files.get(p));
				}
			});
		list.setOnItemLongClickListener(new OnItemLongClickListener(){
				@Override public boolean onItemLongClick(AdapterView<?> a,View v,final int p,long id){
					new AlertDialog.Builder(Files.this).setMessage("Delete this file?").setNegativeButton("No",null).setPositiveButton("Yes",new AlertDialog.OnClickListener() {
							@Override public void onClick(DialogInterface p1,int p2){
								File file = new File(getFilesDir(),files.get(p));
								file.delete();
								files.remove(p);
								adapter.notifyDataSetChanged();
							}
						}).show();
					return true;
				}
			});
		Button butt = new Button(this);
		butt.setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
		butt.setText("OK");
		butt.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View v){
					setResult(RESULT_OK,new Intent().putExtra("file",edit.getText().toString()));
					finish();
				}
			});
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(1);
		ll.addView(edit);
		ll.addView(butt);
		ll.addView(list);
		setContentView(ll);
	}
}
