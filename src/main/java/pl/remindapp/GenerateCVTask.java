package pl.remindapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import pl.remindapp.activities.ChooseCvTemplateActivity;
import pl.remindapp.activities.SuccessfullActivity;

public class GenerateCVTask extends AsyncTask<PdfGenerator,Void,Void> {
    private int numberOfPage;
    String filePath;
    private ChooseCvTemplateActivity callingActivity;

    public GenerateCVTask(ChooseCvTemplateActivity activity){
        callingActivity = activity;

    }

    @Override
    protected void onPreExecute() {
        callingActivity.setAnimate();
    }

    @Override
    protected Void doInBackground(PdfGenerator... pdfGenerators) {
        int maxFontSize = 30;
        filePath = pdfGenerators[0].getFilePath();
        numberOfPage = pdfGenerators[0].generateCv();

        while( numberOfPage > 1){
            System.out.println(maxFontSize);
            maxFontSize--;
            pdfGenerators[0].setFontSize(maxFontSize);
            numberOfPage = pdfGenerators[0].generateCv();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent intent = new Intent(callingActivity, SuccessfullActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        callingActivity.startActivity(intent);
        callingActivity.finish();
        if(numberOfPage == 1)
            Toast.makeText(callingActivity, "File created in: " + filePath , Toast.LENGTH_LONG).show();
        else
            Toast.makeText(callingActivity, "Creating file failed", Toast.LENGTH_LONG).show();
    }
}
