package project;

public class Track{
        private int track_id;
        private int track_duration;
        private int track_individual_value;
        private double[] track_sequential_value;
        
        public Track(int track_id, int track_duration, int track_individual_value, double[] track_sequential_value){
            this.track_id = track_id;
            this.track_duration = track_duration;
            this.track_individual_value = track_individual_value;
            this.track_sequential_value = track_sequential_value;
        }
        
        public double getRealTrackValue() {
        	//
        	// real value is a value that I created in order to decide that
        	// which song is more worthy to include the selected track list
        	// track with higher individual value and shorter duration has higher real value
        	//
        	return (double)getIndividualValue() / (double)getDuration();
        }
        public double getSeqValueOf(int targetTrackId) {
        	return getSequentialValue()[targetTrackId];
        }
        public int getId(){
            return track_id;
        }
        public void setId(int track_id){
            this.track_id = track_id;
        }
        public int getDuration(){
            return track_duration;
        }
        public void setDuration(int track_duration){
            this.track_duration = track_duration;
        }
        public int getIndividualValue(){
            return track_individual_value;
        }
        public void setIndividualValue(int track_individual_value){
            this.track_individual_value = track_individual_value;
        }
        public double[] getSequentialValue(){
            return track_sequential_value;
        }
        public void setSequentialValue(double[] track_sequential_value){
            this.track_sequential_value = track_sequential_value;
        }
        public String toString() {
        	return "Track Id: " + getId() + ", Duration: " + getDuration() + ", Individual Value: " + 					getIndividualValue() + ", Real Value: " + getRealTrackValue();
        }
}