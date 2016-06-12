package FSS;

/**
 * Created by moura on 6/12/16.
 */
public class Fish {

    public FSS_Solution neighbor = null;
    public FSS_Solution current = null;
    public FSS_Solution best = null;

    public double current_weight = Double.NaN;

    public double past_weight = Double.NaN;

    public boolean individual_move_success = false;

    public boolean instinctive_move_success = false;

    public boolean volitive_move_success = false;

    public double[] delta_x = null;
    public double delta_f = Double.NaN;
    public double fitness_gain_normalized = Double.NaN;

    public Fish(){

    }

}
