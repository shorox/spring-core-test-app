package quoters;

public class ProfilingController implements ProfilingControllerMBean {
  private boolean enabled;

  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
