package pso;

public class Particle
{
  public double[] position; // equivalent to NN weights
  public double error; // measure of fitness
  public double[] velocity;

  public double[] bestPosition; // best position found so far by this Particle
  public double bestError;

  //public double age; // optional used to determine death-birth

  public Particle(double[] position, double error, double[] velocity,
    double[] bestPosition, double bestError)
  {
      this.position = new double[position.length];
      for(int i = 0; i < position.length; i++){
    	  this.position[i] = position[i];
      }
      

      this.error = error;

      this.velocity = new double[velocity.length];
      
      for(int i = 0; i < velocity.length; i++){
    	  this.velocity[i] = velocity[i];
      }
      
      
      this.bestPosition = new double[bestPosition.length];
      
      for(int i = 0; i < bestPosition.length; i++){
    	  this.bestPosition[i] = bestPosition[i];
      }
      
      
      this.bestError = bestError;
  }

  //public override string ToString()
  //{
  //  string s = "";
  //  s += "==========================\n";
  //  s += "Position: ";
  //  for (int i = 0; i < this.position.Length; ++i)
  //    s += this.position[i].ToString("F2") + " ";
  //  s += "\n";
  //  s += "Error = " + this.error.ToString("F4") + "\n";
  //  s += "Velocity: ";
  //  for (int i = 0; i < this.velocity.Length; ++i)
  //    s += this.velocity[i].ToString("F2") + " ";
  //  s += "\n";
  //  s += "Best Position: ";
  //  for (int i = 0; i < this.bestPosition.Length; ++i)
  //    s += this.bestPosition[i].ToString("F2") + " ";
  //  s += "\n";
  //  s += "Best Error = " + this.bestError.ToString("F4") + "\n";
  //  s += "==========================\n";
  //  return s;
  //}

} // class Particle

 // ns
