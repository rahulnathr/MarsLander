package MarsLanding;

import java.util.Scanner;

public class Refresh {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int N = in.nextInt(); // the number of points used to draw the surface of Mars.
        int Xprev = 0;
        int Yprev = 0;
        int XflatOne = 0;
        int XflatTwo = 0;
        int landingY = 0;
        for (int i = 0; i < N; i++) {
            int landX = in.nextInt(); // X coordinate of a surface point. (0 to 6999)
            int landY = in.nextInt(); // Y coordinate of a surface point. By linking all the points together in a sequential fashion, you form the surface of Mars.
            if (landY == Yprev) {
                XflatTwo = landX;
                XflatOne = Xprev;
                landingY = landY;
            }
            Xprev = landX;
            Yprev = landY;
        }
        int thrust = 4;
        int rotation = 0;

        double integralNotOverTarget = 0;
        double lastErrorNotOverTarget = 0;

        double integralOverTarget = 0;
        double lastErrorOverTarget = 0;


        double integralPosition = 0;
        double lastErrorPosition = 0;


        // game loop
        while (true) {
            int X = in.nextInt();
            int Y = in.nextInt();
            int HS = in.nextInt(); // the horizontal speed (in m/s), can be negative.
            int VS = in.nextInt(); // the vertical speed (in m/s), can be negative.
            int F = in.nextInt(); // the quantity of remaining fuel in liters.
            int R = in.nextInt(); // the rotation angle in degrees (-90 to 90).
            int P = in.nextInt(); // the thrust power (0 to 4).

            int targetPosition = (XflatOne + XflatTwo) / 2;
            int targetHsSpeedOverLanding = 20;
            int targetHsWhileHovering = 40;


            double Kph = 10;
            double Kih = 0.0;
            double Kdh = 2.0;

            double Kpp = 10.5;
            double Kip = 0.0;
            double Kdp = 4.0;
            System.err.println("Start " + XflatOne);
            System.err.println("End " + XflatTwo);
            System.err.println("Target " + targetPosition);

            if (!isOverTargetArea(X, XflatOne, XflatTwo)) {
                if (findDirectionOfHorizontalSpeed(X, XflatOne, XflatTwo) == -1) {
                    targetHsWhileHovering = -targetHsWhileHovering;
                }
                System.err.println("Not over target " + targetHsWhileHovering + " abs " + Math.abs(HS));
                double errorHs = targetHsWhileHovering - HS;
                integralNotOverTarget += errorHs;
                double errorDiff = errorHs - lastErrorNotOverTarget;
                lastErrorNotOverTarget = errorHs;

                double errorPosition = targetPosition - X;
                System.err.println("Position error " + errorPosition);
                integralPosition += errorPosition;
                integralPosition = Math.max(-1000, Math.min(1000, integralPosition));
                double errorDiffPos = errorPosition - lastErrorPosition;
                lastErrorPosition = errorPosition;

                double totalError = -((Kph * errorHs) + (Kih * integralNotOverTarget) + (Kdh * errorDiff) +
                        (Kpp * errorPosition) + (Kip * integralPosition) + (Kdp * errorDiffPos));
                System.err.println("Total error " + totalError);
                if (Math.abs(HS) > 56) {
                    System.err.println("Block 11");
                    totalError = getOptimalBrakingAngle(HS, VS);
                } else if (Math.abs(VS) > 23) {
                    totalError = 0;
                    System.err.println("Block 21");
                } else {
                    System.err.println("Block 31");
                    totalError = Math.max(-90, Math.min(90, totalError));
                }
                rotation = (int) totalError;
            } else {
                double errorHs = 5 - HS;
                integralOverTarget += errorHs;
                double errorDiff = errorHs - lastErrorOverTarget;
                lastErrorOverTarget = errorHs;

                double errorPosition = targetPosition - X;
                integralPosition += errorPosition;
                double errorDiffPos = errorPosition - lastErrorPosition;
                lastErrorPosition = errorPosition;

                double totalError = -((Kph * errorHs) + (Kih * integralNotOverTarget) + (Kdh * errorDiff) +
                        (Kpp * errorDiffPos) + (Kip * integralPosition) + (Kdp * errorDiffPos));

                if (Math.abs(HS) > 40) {
                    System.err.println("Block 1");
                    totalError = getOptimalBrakingAngle(HS, VS);
                } else if (Math.abs(VS) > 38) {
                    System.err.println("Block 2");
                    totalError = 0;
                } else {
                    totalError = Math.max(-90, Math.min(90, totalError));
                    System.err.println("Block 3");
                }

                rotation = (int) totalError;
            }
            System.err.println("HS give " + (Math.abs(HS) <= 20));
            System.err.println("VS give " + (Math.abs(VS) <= 40));
            System.err.println("Y give " + (Math.abs(Y) < 250));

            if (Math.abs(HS) <= 20 && Math.abs(VS) <= 40 && Math.abs(Y) < 200) {
                rotation = 0;
                System.err.println("Come here.");
            }
            System.err.println("Rotation give " + rotation);

            System.out.println(rotation + " " + thrust);
        }
    }

    static boolean isOverTargetArea(int X, int start, int end) {
        return X >= start && X <= end;
    }

    static int findDirectionOfHorizontalSpeed(int X, int start, int end) {
        if (X >= start && X >= end) {
            return -1;
        } else if (X <= start && X <= end) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int getOptimalBrakingAngle(int horizontalSpeed, int verticalSpeed) {
        double hs = horizontalSpeed;
        double vs = verticalSpeed;
        double speed = Math.hypot(hs, vs);
        return (int) Math.toDegrees(Math.asin(hs / speed));
    }
}

