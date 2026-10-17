const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read saved videos or words", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("saved_videos").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("saved_words").get());
});

test("Cross-user access: Bob cannot read Alice's saved videos or words", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(ALICE_UID).collection("saved_videos").doc("vid_1").set({
      userId: ALICE_UID,
      videoId: "vid_1",
      title: "Alice Video",
      author: "Channel",
      summaryPersian: "خلاصه",
      createdAt: new Date(),
    });
  });

  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("saved_videos").doc("vid_1").get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("saved_videos").get());
});

test("Authenticated owner: can create and read own saved video", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("saved_videos").doc("vid_1").set({
      userId: ALICE_UID,
      videoId: "vid_1",
      title: "Kotlin Basics",
      author: "Tech",
      summaryPersian: "آموزش مقدماتی کاتلین",
      createdAt: new Date(),
    })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("saved_videos").doc("vid_1").get()
  );
});

test("Authenticated owner: cannot create saved video with mismatched userId", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.collection("users").doc(ALICE_UID).collection("saved_videos").doc("vid_1").set({
      userId: BOB_UID, // Mismatched!
      videoId: "vid_1",
      title: "Spoofed Video",
      author: "Tech",
      summaryPersian: "تست",
      createdAt: new Date(),
    })
  );
});

test("Authenticated owner: can create and read own saved word", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("saved_words").doc("word_1").set({
      userId: ALICE_UID,
      word: "Subtitles",
      phonetic: "/sʌbtaɪtlz/",
      persianMeaning: "زیرنویس‌ها",
      createdAt: new Date(),
    })
  );

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("saved_words").doc("word_1").get()
  );
});
