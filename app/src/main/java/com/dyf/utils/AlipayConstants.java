package com.dyf.utils;

import java.util.Date;

/**
 * Created by diy on 2018/4/18.
 * 支付相关的constants
 */

public class AlipayConstants {

    //    2018011501876399
    public static String APPID = "2016081500253471";

    public static String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCfVgaGAPIFyP2L\n" +
            "RmlYvHrXj6l++8qRvncGddrSy2xIdAeojWLJYMo6hcoCH+tZzaP9A6Owyd2Vjq1p\n" +
            "eucrjxLg5keR96Y7zgF9i6ihBoeqoGdHMcE+z+M+zruN0qG4SYeeMrjRSBUxqCM0\n" +
            "Vp+1jhTDQr3SBFgwZ4uRtmXLe78oxPwXFyz1TBloKwVNwb6aGxGBne5ZtD9+8uOk\n" +
            "oYw/stjGI10C2VkuWo4EeegZEsR/jMamiupgJKGmIcH5pfInXaIJKlfFmDCx93Oc\n" +
            "M584Xv/euhjYR3OPc+F3pz10HdoVWETI1w9SkqOXL1y7PVBWMjfaDbUTngZY3/uP\n" +
            "qet0J1IvAgMBAAECggEAd2hmtyIaPk+kAy4fYMrPXKBjK7DS3GpalNLZzMN6QMoD\n" +
            "u0NJ1xTcOQNuoiz9mW1NCh4iHCkMEDq+pp/egnxUb4rj8/Yd9A+sekOhehuErFBN\n" +
            "VcNpyQLOkJEpaV2CqLMiGWJ1sxFMIMyapWyNf+gck0B/NNbgqFXQ+MAyhU4VshzO\n" +
            "9HK5C4xNQ5z7dfy++BNb0BAzRURuq4CEqZ8u39ZIAF4uSIebYEPNV4il6aDd+Scg\n" +
            "JLqJuZF+LM3ghndOWBSwMyz5MEt9zDZFMu2Ko+drGZ6OMpZ1KmrDiU8zcaQs0vP3\n" +
            "WFVPv4MFXqqNQUQ73OBnbdo3+7RG+X07XbIk3VqhwQKBgQDNiJXRQ2bOF1G1Sixn\n" +
            "jO8Os0YDQYpqJ6fipaiIp3mbS6hliy7BnpRw3JVSuMtZWrpS73Nc1/qZyK89eJpv\n" +
            "SiGOlpxquC6O+2eL4sbX8GZgkV/H8q0ZoRuVBrv965N70i53l7e10nAQUCsEBR1u\n" +
            "XL4Kh1oYqWdYgOqBnmyuP/rF/wKBgQDGdY+EgZyb2OaT37iiGEMaMRNxGLpXW8ek\n" +
            "HGcLYGdReugkNOgxNlrYcy4qAqkvBjruqklxd80rKV7O1KGc0zC2KEWo7j04zNw6\n" +
            "SCq1coOhebuPDba/4VsnvDkrSL4/6Pw7xpwPaF+DfvoxeUSFDZUjcGdK3NeveJeo\n" +
            "5qEgOcZT0QKBgQCzPXbx3ObNudZe1EWvNF/SIRNgiYqtEtI9PJyiN/M8NBmyHFP1\n" +
            "NFddqXBriI6F6TJuWzXu9Mctz67LW97KSQrEr6HPC7pGpQZjHqyr6pFNi9nMpFzC\n" +
            "6hYEu6+8O/INXHl7gAsM/G0QPOmWguNsuGJfCjTPJwTiUOKxkThUKaWHfwKBgFdF\n" +
            "DQZUxJxXxhVStOs6xg83c3gxSoftyR2mi72Z9/UBmiGJzm0NnOc/xxYcM2+FStb2\n" +
            "tPMbnnITcCh9ok/HIs2HahJ+KRfYJiPO5lEB+VZBpLOkcS9xltcjUvRwuZB7EKfs\n" +
            "j2XLHO1DIhwtWm8z6Ng7roBiGsqDuhVAzavRHCBxAoGAQxA+u0UPXJvTRjVnrsxp\n" +
            "swQQ1WqF91rx55sRc3MXVGuDZqihY8w0MyS1xg0Z+XSjyY6CBbiNy4ZJ/qYXpiEA\n" +
            "hMnB5N1/7Fu6CComXg7/i8ujngZKgzyvCZ5TZfxTwmFPcmqzzm2lsMmnrzTtBv0O\n" +
            "kHwIMeDlqUOfEipJ3nxEBdI=";
    public static String RSA_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCnwI4G8DE4rCIlXsS6OMFv3FKxaAyF5ONLVSB3KS9TyQGlhYPZw1TSN1UVBOdFJ2O0P6Xw0PRBvn+igjeeky8UxJtU+uhqBpP4pqzs9KpifkAiczJPjnvzhE18ZyrWHVCic+K9MxDfOoDiiEMJ3iw+FanKt/f3g9F+b86VjD7qEHuFFMCpVhcYr8sHmdHSPrOOjtGR1/vZk7oe3W+OHFDfnwdKNth5BnBdKfbHHxt4IzYnFnu1UC3Q9D/BhtxMl+ffITxBE5xnMqhEPiZMMeS75eG+rkBIdWoY/pREn+6PDGIe1XaJdKHGpAfwi8X+Zkgbe5tq6sDM8pXThg7+OPb1AgMBAAECggEBAIJP5ISjRbk+jm/1RGFdAXdB8cL6ju/cNG0qvcOTbL5rYSoRo7EgOd3rOd7QJk/kH2HAJ8oAqB4CO+gju80ljOdXK0tCdhUW8w+vepnAHjPQ20Vns2d1yfgLiJ3hDrexohfwaeyLpMmkCsbvDxS1UC0SawmdC7Vs4uqTNCmb/quXy8I+dzivak0EenbWVO/bEJo/3/u3r6OnULu5bxxYfrcLMtpgLp+/PIaxfAElRI4grAk3onXGtWTD/rMnFksXIRWF0mH6R8kb5Ke93fFzRKvQVTk7bZ3bbfzIZrWKTh+8e3eZ0QuHjvzrR73gVMLfrQFBTHeXraXZZdsSyPOSfcECgYEA20J8TiEGH3FreLy7AUPc5TquVoC49/oRHUQaW+KykoVdiio06nk86zW5JyAOZ/uIHoOREMm2mB03S1Um9yWvJPi5nX55hJEcy5ioSbuI4DFid4HO+Ugvm4nj1vf7b5qglceFMb9fLxPUoeuhOmlkS3EZZkLmfLVaosDqeo9iDF0CgYEAw9yRBJqknKVP2W5q8IGW70HxYqP8EgbMRyyN8RMh6+SPQoFQQiLWHsEhkZ46lG8SsvBeOi/uXoCH96LDgxsTOJAujrNAbSmhVUkGR6ex07gYtJS/JB33M9JNmZ4ZTynzhVvNdFyzgr86qaqk/4XRzf8fP/FlhUilatGOWtMrq3kCgYEAlSjdt6jvqWRXYIPEFFA+bhNFlsNrEgekrOwvomLOaK8hu2SLKxffQYQCuYMtSb/sEhYfEfSnq0P04eLD505ToCs02te3g1/U3wyzL7XMn/f4rKQ6UYuyF3Xu8ccx3fKrXDmnevhdoIE1HhJWVI5uFISUauUIN76IMkBbQ5VXdmECgYAPoJCdim0p5nK5iXrU17IgQuWsliLtmUdBQofMvVcOiDz59HFf5YGqEITKJpLL9xTJ3C3YfKyLM7wlgQISet2MvMxKLHkufIzXzizHGNUybFTkS534lr97jgMNB7VDQLiiYlHBQAg+nV1j3i6uCJJV2k42/t63xLUWEcRG8ORiGQKBgGq0cufP9deqyVLpB4sxcBb2E4DSre7NmLbdtkMrnjDCANMucwpwCbOxqsNq4bL/7SCExLTeugqm9c1SrOSu6RwfI3Wo8Rg1/gc+p9KnGPIJ5AJONA3hf95Ps5Bx1tj34fxXqbXrbHjrxW9IkQ4VSAIMdzUwyYQMXTqCUzw/AN3v";

    public static String TARGET_ID = new Date().toString();

}
