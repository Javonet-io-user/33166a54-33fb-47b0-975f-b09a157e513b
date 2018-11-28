package Common;

import com.javonet.api.NObject;

public interface INEventHandler {
  <T1> void Invoke(NObject sender, T1 e);

  <T1, T2> void Invoke(NObject sender, T1 e, T2 ee);
}
