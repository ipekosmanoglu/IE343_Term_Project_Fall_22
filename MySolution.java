package project;

public class MySolution {
	public static void main(String[] args) {
		
		final int MAX_DURATION = 1800; // max album duration in seconds
		final double VAL_LOSS_PER_SEC = 0.02; // value loss per missing second
		
		long startTime = System.nanoTime();
		
		Track[] trackList = ExcelReader.ReadTracksFromExcelSheets();
		
		if (trackList == null)
			return;
		
		// PART 1: SELECTING TRACKS
		
		System.out.println("--------------------------------");
		System.out.println("--- PART 1: Selecting Tracks ---");
		System.out.println("--------------------------------\n");
		
		trackList = sortByRealValue(trackList);
		
		Track[] selectedTracks = new Track[trackList.length];
		
		int selectedTrackCount = 0; // count of selected tracks
		int totalDuration = 0; // total duration of selected tracks in seconds
		int totalAlbumValue = 0; // total individual value of selected tracks
		
		// select tracks starting from the first track of sorted track list
		// which means the track that has the highest real value
		// and calculate raw total album value and total album duration
		for (int trackIndex = 0; trackIndex < trackList.length; trackIndex++) {
			int nextTotalDuration = totalDuration + millisecToSec(trackList[trackIndex].getDuration());
			if (nextTotalDuration <= MAX_DURATION) {
				selectedTracks[selectedTrackCount++] = trackList[trackIndex];
				totalAlbumValue += trackList[trackIndex].getIndividualValue();
				totalDuration = nextTotalDuration;
			}
		}
		// decrease total album value according to missing seconds
		totalAlbumValue -= (MAX_DURATION - totalDuration) * VAL_LOSS_PER_SEC;
		
		// print the selected tracks
		for (int trackIndex = 0; trackIndex < selectedTracks.length; trackIndex++) {
			if (selectedTracks[trackIndex] != null)
				System.out.println((trackIndex + 1) + ") " + selectedTracks[trackIndex].toString());
		}
		
		// PART 2: SORTING TRACKS
		
		System.out.println("\n------------------------------");
		System.out.println("--- PART 2: Sorting Tracks ---");
		System.out.println("------------------------------\n");
		
		// final track array that label wants
		Track[] finalTracks = new Track[selectedTrackCount];
		
		// set first and last track of final track array
		// when we use takeTrack function it returns the most valuable track in this case
		// and makes it null, so when we call takeTrack function again, it returns the
		// second most valuable song as a result.
		finalTracks[0] = takeTrack(selectedTracks, getMostValuableTrackIndex(selectedTracks));
		finalTracks[finalTracks.length - 1] = takeTrack(selectedTracks,
				getMostValuableTrackIndex(selectedTracks));
		
		// select the remained tracks according to their most suitable track index
		// like above, it decides the most suitable track among the unselected tracks
		for (int trackIndex = 1; trackIndex < finalTracks.length - 1; trackIndex++) {
			finalTracks[trackIndex] = takeTrack(selectedTracks,
					getMostSuitableTrackIndex(finalTracks[trackIndex - 1], selectedTracks));
		}
		
		// print the final tracks
		for (int trackIndex = 0; trackIndex < finalTracks.length; trackIndex++) {
			if (finalTracks[trackIndex] != null)
				System.out.println((trackIndex + 1) + ") " + finalTracks[trackIndex].toString());
		}
		
		long endTime = System.nanoTime();
		double runtimeDuration = (endTime - startTime) / 1000000000.0; // runtime duration in milliseconds
		
		// PART 3: SHOWING RESULTS
		
		System.out.println("\n-------------------------------");
		System.out.println("--- PART 3: Showing Results ---");
		System.out.println("-------------------------------\n");
		
		int exactSolAlbumValue = 532;
		double exactSolRuntimeDuration = 0.024909496307373047;
		
		System.out.println("SELECTED TRACK COUNT: " + selectedTrackCount);
		System.out.println("\nTOTAL ALBUM DURATION: " + totalDuration + " secs (" + String.format("%.2f", totalDuration / 60.0) + " mins)");
		System.out.println("\nCOMPARISON");
		System.out.format("\nMY ALBUM VALUE: %d || EXACT SOLUTION ALBUM VALUE: %d", totalAlbumValue, exactSolAlbumValue);
		System.out.format("\nMY RUNTIME DURATION: %f || EXACT SOLUTION RUNTIME DURATION: %f", runtimeDuration, exactSolRuntimeDuration);
		
	}
	
	private static Track takeTrack(Track[] trackArray, int trackIndex) {
		//
		// makes a copy of the track
		// sets the track as null to make sure
		// we don't use it again in sorting part
		// and returns copy of the track
		//
		Track track = trackArray[trackIndex];
		trackArray[trackIndex] = null;
		return track;
	}
	
	private static int getMostSuitableTrackIndex(Track track, Track[] trackArray) {
		//
		// checks all non-null tracks in track array
		// and returns the track index that has
		// highest sequential value with the given track
		//
		int mostSuitableTrackIndex = 0;
		double highestSeqValue = 0;
		
		for (int trackIndex = 0; trackIndex < trackArray.length; trackIndex++) {
			if (trackArray[trackIndex] == null)
				continue;
			if (track.getSeqValueOf(trackArray[trackIndex].getId()) > highestSeqValue) {
				highestSeqValue = track.getSeqValueOf(trackArray[trackIndex].getId());
				mostSuitableTrackIndex = trackIndex;
			}
		}
		return mostSuitableTrackIndex;
	}
	
	private static int getMostValuableTrackIndex(Track[] trackArray) {
		//
		// returns the track index that has the highest individual value in the track array
		//
		int highestTrackValue = 0;
		int highestTrackIndex = 0;
		for (int trackIndex = 0; trackIndex < trackArray.length; trackIndex++) {
			if (trackArray[trackIndex] != null) {
				if (trackArray[trackIndex].getIndividualValue() > highestTrackValue) {
					highestTrackValue = trackArray[trackIndex].getIndividualValue();
					highestTrackIndex = trackIndex;
				}				
			}
		}
		return highestTrackIndex;
	}
	
	private static Track[] sortByRealValue(Track[] trackArray) {
		//
		// sorts the track array by descending order according to their real value
		//
		Track[] newTrackArray = new Track[trackArray.length];
		for (int trackIndex = 0; trackIndex < trackArray.length; trackIndex++) {
			newTrackArray[trackIndex] = trackArray[trackIndex];
		}
		for (int index1 = 0; index1 < newTrackArray.length; index1++) {
			for (int index2 = 0; index2 < newTrackArray.length; index2++) {
				if (newTrackArray[index1].getRealTrackValue() > newTrackArray[index2].getRealTrackValue()) {
					Track trackTemp = newTrackArray[index1];
					newTrackArray[index1] = newTrackArray[index2];
					newTrackArray[index2] = trackTemp;
				}
			}
		}
		return newTrackArray;
	}
	
	private static int millisecToSec(int millisecs) {
		//
		// converts the given value milliseconds to seconds
		//
		return millisecs / 1000;
	}
	
}
