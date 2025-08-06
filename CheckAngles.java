public class CheckAngles {
    public static void main(String[] args) {

        int Hs = 10;
        int Vs = -39;


//        System.out.println(Math.toDegrees(Math.atan2(Hs, Vs)));
        printAngles((int) Math.toDegrees(Math.atan2(Hs, Vs)));

        Hs = 40;
        Vs = -10;


        printAngles((int) Math.toDegrees(Math.atan2(Hs, Vs)));

        Hs = -40;
        Vs = +10;


//        System.out.println(Math.toDegrees(Math.atan2(Hs, Vs)));
        printAngles((int) Math.toDegrees(Math.atan2(Hs, Vs)));

        System.out.println(Math.toDegrees(Math.acos(3.71 / 4)));

        System.out.println("HS : 20, Vs -10 " + getOptimalBrakingAngle(20, -10));
        System.out.println("HS : 20, Vs 1 " + getOptimalBrakingAngle(20, 1));
        System.out.println("HS : -20, Vs -10 " + getOptimalBrakingAngle(-20, -10));
    }

    static void printAngles(int angle) {
        System.out.println("Before clamp " + angle);
        System.out.println("After clamp " + Math.max(-90, Math.min(angle, 90)));
    }

    static int getOptimalBrakingAngle(int horizontalSpeed, int verticalSpeed) {
        double hs = horizontalSpeed;
        double vs = verticalSpeed;
        double speed = Math.hypot(hs, vs);
        return (int) Math.toDegrees(Math.asin(hs / speed));
    }
}
